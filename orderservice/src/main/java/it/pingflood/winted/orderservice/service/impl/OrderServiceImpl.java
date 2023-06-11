package it.pingflood.winted.orderservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pingflood.winted.orderservice.client.AddressClient;
import it.pingflood.winted.orderservice.client.PaymentClient;
import it.pingflood.winted.orderservice.client.ProductClient;
import it.pingflood.winted.orderservice.client.data.PaymentResponse;
import it.pingflood.winted.orderservice.client.data.*;
import it.pingflood.winted.orderservice.data.Offerta;
import it.pingflood.winted.orderservice.data.Order;
import it.pingflood.winted.orderservice.data.OrderStatus;
import it.pingflood.winted.orderservice.data.dto.*;
import it.pingflood.winted.orderservice.event.NewOrderEvent;
import it.pingflood.winted.orderservice.repository.OrderRepository;
import it.pingflood.winted.orderservice.service.OrderService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {
  
  private final OrderRepository orderRepository;
  private final ModelMapper modelMapper;
  private final ProductClient productClient;
  private final AddressClient addressClient;
  private final PaymentClient paymentClient;
  private final ObjectMapper objectMapper;
  private final KafkaTemplate<String, NewOrderEvent> newOrderEventKafkaTemplate;
  private final String tokenPrefix;
  
  public OrderServiceImpl(OrderRepository orderRepository, ProductClient productClient, AddressClient addressClient, PaymentClient paymentClient, ObjectMapper objectMapper, KafkaTemplate<String, NewOrderEvent> newOrderEventKafkaTemplate) {
    this.tokenPrefix = "Bearer ";
    this.orderRepository = orderRepository;
    this.productClient = productClient;
    this.addressClient = addressClient;
    this.paymentClient = paymentClient;
    this.objectMapper = objectMapper;
    this.newOrderEventKafkaTemplate = newOrderEventKafkaTemplate;
    modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
      .setFieldMatchingEnabled(true)
      .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
  }
  
  @Override
  public OrderResponse getOne(UUID id, String principal) {
    return modelMapper.map(orderRepository.findById(id).orElseThrow(), OrderResponse.class);
  }
  
  @Override
  public List<OrderResponse> getAll(String principal) {
    return orderRepository.findAll()
      .stream()
      .map(order -> modelMapper.map(order, OrderResponse.class))
      .toList();
  }
  
  @Override
  public List<OrderResponse> getAllByUser(String userId, String principal) {
    return orderRepository.findAllByBuyer(userId)
      .stream()
      .map(order -> modelMapper.map(order, OrderResponse.class))
      .toList();
  }
  
  @Override
  public OrderResponse confirmOrder(OrderConfirmRequest orderConfirmRequest, String principal, String token) {
    Order order = orderRepository.findById(UUID.fromString(orderConfirmRequest.getId())).orElseThrow();
    
    AddressResponse addressResponse = getAddress(token, order.getAddress());
    ProductResponse productResponse = getProduct(token, order.getProduct());
    PaymentMethodResponse paymentMethodResponse = getPaymentMethod(token, order.getPaymentMethod());
    
    if (productResponse == null || productResponse.getId().isEmpty() ||
      addressResponse == null || addressResponse.getId().toString().isEmpty() ||
      paymentMethodResponse == null || paymentMethodResponse.getId().isEmpty()) {
      throw new IllegalArgumentException("Errore - L'ordine deve avere un prodotto, un indirizzo e un metodo di pagamento validi");
    }
    if (addressResponse.getUser() == null || !addressResponse.getUser().equals(principal) ||
      paymentMethodResponse.getUser() == null || !paymentMethodResponse.getUser().equals(principal) ||
      productResponse.getBought().equals("true") || productResponse.getDraft().equals("true")) {
      throw new IllegalArgumentException("Errore nei dati dell'ordine");
    }
    PaymentResponse paymentResponse = null;
    BigDecimal price = productResponse.getPrice();
    if (order.getOfferta() != null) {
      price = BigDecimal.valueOf(order.getOfferta().getPrice());
    }
    try {
      paymentResponse = makePayment(token, order, String.valueOf(price));
      order.setStatus(OrderStatus.PAYED);
      setProductBoughtStatus(token, order.getProduct());
      sendMessageToOwner(order.getOwner(), order.getBuyer(), order.getProduct());
    } catch (Exception e) {
      if (paymentResponse != null && paymentResponse.getStatus().equals("LOCAL")) {
        makeRefund(token, paymentResponse.getId());
      }
      undoSetProductBoughtStatus(token, order.getProduct());
      order.setStatus(OrderStatus.PAYMENTERROR);
    }
    return modelMapper.map(order, OrderResponse.class);
  }
  
  @Override
  public OrderResponse createPreorder(OrderRequest orderRequest, String principal, String token) {
    if (orderRepository.findByBuyerAndProduct(principal, orderRequest.getProduct()).isPresent()) {
      return modelMapper.map(orderRepository.findByBuyerAndProduct(principal, orderRequest.getProduct()), OrderResponse.class);
    }
    ProductResponse productResponse = getProduct(token, orderRequest.getProduct());
    if (productResponse == null || productResponse.getBought().equals("true") || productResponse.getDraft().equals("true")) {
      throw new IllegalArgumentException("Il prodotto non esiste o non e' acquistabile!");
    }
    String ownerId = productResponse.getOwner();
    if (principal.equals(ownerId)) {
      throw new IllegalArgumentException("Non puoi comprare un tuo oggetto!");
    }
    log.info("token\n{}", token);
    AddressResponse adr = getAddressByUser(token, principal);
    String addressId = null;
    if (adr != null) {
      addressId = adr.getId().toString();
    }
    PaymentMethodResponse paymentMethodResponse = getPaymentMethodByUser(token, principal);
    String paymentMethodId = null;
    if (paymentMethodResponse != null) {
      paymentMethodId = paymentMethodResponse.getId();
    }
    return modelMapper.map(
      orderRepository.save(Order.builder()
        .buyer(principal)
        .owner(ownerId)
        .product(orderRequest.getProduct())
        .status(OrderStatus.NEW)
        .address(addressId)
        .paymentMethod(paymentMethodId)
        .build()), OrderResponse.class);
  }
  
  @Override
  public OrderResponse updatePreorder(UUID id, OrderUpdateRequest orderRequest, String principal, String token) {
    Order older = orderRepository.findById(id).orElseThrow();
    if (!principal.equals(older.getBuyer())) {
      throw new IllegalArgumentException("Cant modify this object");
    }
    
    if (orderRequest.getAddressId() != null && !orderRequest.getAddressId().equals("")) {
      older.setAddress(orderRequest.getAddressId());
    }
    if (orderRequest.getPaymentMethodId() != null && !orderRequest.getPaymentMethodId().equals("")) {
      older.setPaymentMethod(orderRequest.getPaymentMethodId());
    }
    orderRepository.save(older);
    return modelMapper.map(older, OrderResponse.class);
  }
  
  @Override
  public OrderResponse createPreorderService(OrderRequestService orderRequestByService, String principal, String token) {
    Offerta offerta = Offerta.builder().price(orderRequestByService.getPrice()).build();
    String buyer = orderRequestByService.getBuyer();
    if (orderRepository.findByBuyerAndProduct(buyer, orderRequestByService.getProduct()).isPresent()) {
      Order older = orderRepository.findByBuyerAndProduct(buyer, orderRequestByService.getProduct()).orElseThrow();
      older.setOfferta(offerta);
      return modelMapper.map(orderRepository.save(older), OrderResponse.class);
    }
    
    ProductResponse productResponse = getProduct(token, orderRequestByService.getProduct());
    if (productResponse == null || productResponse.getBought().equals("true") || productResponse.getDraft().equals("true")) {
      throw new IllegalArgumentException("Il prodotto non esiste o non e' acquistabile!");
    }
    
    String ownerId = productResponse.getOwner();
    
    if (!principal.equals(ownerId)) {
      throw new IllegalArgumentException("Non puoi creare un'offerta per un oggetto non tuo!");
    }
    
    log.info("token\n{}", token);
    AddressResponse adr = getAddressByUser(token, buyer);
    
    String addressId = null;
    
    if (adr != null) {
      addressId = adr.getId().toString();
    }
    
    PaymentMethodResponse paymentMethodResponse = getPaymentMethodByUser(token, buyer);
    String paymentMethodId = null;
    
    if (paymentMethodResponse != null) {
      paymentMethodId = paymentMethodResponse.getId();
    }
    
    return modelMapper.map(
      orderRepository.save(Order.builder()
        .buyer(buyer)
        .owner(ownerId)
        .product(orderRequestByService.getProduct())
        .status(OrderStatus.NEW)
        .address(addressId)
        .offerta(offerta)
        .paymentMethod(paymentMethodId)
        .build()), OrderResponse.class);
  }
  
  private PaymentResponse makePayment(String token, Order order, String price) {
    try {
      String res = this.paymentClient.makePayment(this.tokenPrefix + token, PaymentRequest.builder()
        .from(order.getBuyer()).to(order.getOwner()).paymentMethodId(order.getPaymentMethod()).importo(price)
        .build());
      log.info("Risposta da makePayment {}", res);
      PaymentResponse obj = objectMapper.readValue(res, PaymentResponse.class);
      log.info("Risposta da makePayment obj {}", obj);
      return obj;
    } catch (Exception e) {
      throw new IllegalStateException("Errore nel pagamento");
    }
  }
  
  private void makeRefund(String token, String paymentId) {
    try {
      this.paymentClient.makeRefund(this.tokenPrefix + token, paymentId);
    } catch (Exception e) {
      throw new IllegalStateException("Errore nel refund del pagamento");
    }
  }
  
  
  private ProductResponse getProduct(String token, String productId) {
    var res = productClient.getProductById(this.tokenPrefix + token, productId);
    log.info("Product response (by id) {}", res);
    ProductResponse productResponse = null;
    try {
      productResponse = objectMapper.readValue(res, ProductResponse.class);
      log.info("Product response (by id) object {}", productResponse);
    } catch (Exception e) {
      return null;
    }
    return productResponse;
  }
  
  @SneakyThrows
  private AddressResponse getAddress(String token, String id) {
    var res = addressClient.getAddressById(this.tokenPrefix + token, id);
    log.info("Address response (by id) {}", res);
    AddressResponse adr = null;
    try {
      adr = objectMapper.readValue(res, AddressResponse.class);
      log.info("Address response (by id) object {}", adr);
    } catch (Exception e) {
      return null;
    }
    return adr;
  }
  
  @SneakyThrows
  private AddressResponse getAddressByUser(String token, String userid) {
    var res = addressClient.getAddressByUserId(this.tokenPrefix + token, userid);
    log.info("Address response (by user) {}", res);
    AddressResponse adr = null;
    try {
      adr = objectMapper.readValue(res, AddressResponse.class);
      log.info("Address response (by user) object {}", adr);
    } catch (Exception e) {
      return null;
    }
    return adr;
  }
  
  @SneakyThrows
  private PaymentMethodResponse getPaymentMethod(String token, String id) {
    var res = paymentClient.getPaymentMethodById(this.tokenPrefix + token, id);
    log.info("Payment method response (by id) {}", res);
    PaymentMethodResponse paymentMethodResponse = null;
    try {
      paymentMethodResponse = objectMapper.readValue(res, PaymentMethodResponse.class);
      log.info("Payment method response (by id) object {}", paymentMethodResponse);
    } catch (Exception e) {
      return null;
    }
    return paymentMethodResponse;
  }
  
  @SneakyThrows
  private PaymentMethodResponse getPaymentMethodByUser(String token, String userid) {
    var res = paymentClient.getPaymentMethodByUserid(this.tokenPrefix + token, userid);
    log.info("Payment method response (by user) {}", res);
    PaymentMethodResponse paymentMethodResponse = null;
    try {
      paymentMethodResponse = objectMapper.readValue(res, PaymentMethodResponse.class);
      log.info("Payment method response (by user) object {}", paymentMethodResponse);
    } catch (Exception e) {
      return null;
    }
    return paymentMethodResponse;
  }
  
  private void setProductBoughtStatus(String token, String productId) {
    productClient.setProductBoughtStatus(this.tokenPrefix + token, productId);
  }
  
  private void undoSetProductBoughtStatus(String token, String productId) {
    productClient.undoSetProductBoughtStatus(this.tokenPrefix + token, productId);
  }
  
  @SneakyThrows
  private void sendMessageToOwner(String owner, String buyer, String product) {
    NewOrderEvent newOrderEvent = new NewOrderEvent(product, buyer, owner);
    newOrderEventKafkaTemplate.send("NewOrder", "order-service", newOrderEvent);
  }
}
