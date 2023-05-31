package it.pingflood.winted.orderservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pingflood.winted.orderservice.client.AddressClient;
import it.pingflood.winted.orderservice.client.PaymentClient;
import it.pingflood.winted.orderservice.client.ProductClient;
import it.pingflood.winted.orderservice.client.data.AddressResponse;
import it.pingflood.winted.orderservice.client.data.PaymentMethodResponse;
import it.pingflood.winted.orderservice.client.data.ProductResponse;
import it.pingflood.winted.orderservice.data.Order;
import it.pingflood.winted.orderservice.data.OrderStatus;
import it.pingflood.winted.orderservice.data.dto.OrderConfirmRequest;
import it.pingflood.winted.orderservice.data.dto.OrderRequest;
import it.pingflood.winted.orderservice.data.dto.OrderResponse;
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
  private final KafkaTemplate<String, NewOrderEvent> newOrderEventKafkaTemplate;
  private final ObjectMapper objectMapper;
  
  
  public OrderServiceImpl(OrderRepository orderRepository, ProductClient productClient, AddressClient addressClient, PaymentClient paymentClient, KafkaTemplate<String, NewOrderEvent> newOrderEventKafkaTemplate, ObjectMapper objectMapper) {
    this.orderRepository = orderRepository;
    this.productClient = productClient;
    this.addressClient = addressClient;
    this.paymentClient = paymentClient;
    this.newOrderEventKafkaTemplate = newOrderEventKafkaTemplate;
    this.objectMapper = objectMapper;
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
    String loggedUserId = principal;
    Order order = orderRepository.findById(UUID.fromString(orderConfirmRequest.getId())).orElseThrow();
    
    try {
      
      AddressResponse addressResponse = addressClient.getAddressById(order.getAddress());
      ProductResponse productResponse = productClient.getProductById(order.getProduct());
      PaymentMethodResponse paymentMethodResponse = paymentClient.getPaymentMethodById(order.getPaymentMethod());
      
      
      if (productResponse.getId().isEmpty() ||
        addressResponse.getId().toString().isEmpty() ||
        paymentMethodResponse.getId().isEmpty()) {
        throw new IllegalArgumentException("Errore");
      }
      
      if (!addressResponse.getUser().equals(loggedUserId) ||
        !paymentMethodResponse.getUser().equals(loggedUserId)) {
        throw new IllegalArgumentException("Errore nei dati dell'ordine");
      }
    } catch (Exception e) {
      log.error("Errore! {}", e.getMessage());
    }
    
    try {
      makePayment(order);
      order.setStatus(OrderStatus.PAYED);
    } catch (Exception e) {
      makeRefund(order);
      order.setStatus(OrderStatus.PAYMENTERROR);
    }
    
    setProductBoughtStatus(order.getProduct());
    sendMessageToOwner(order.getOwner(), order.getBuyer(), order.getProduct());
    
    return modelMapper.map(order, OrderResponse.class);
  }
  
  @Override
  public OrderResponse createPreorder(OrderRequest orderRequest, String principal, String token) {
    String loggedUserId = principal;
    if (orderRepository.findByBuyerAndProduct(loggedUserId, orderRequest.getProduct()).isPresent()) {
      return modelMapper.map(orderRepository.findByBuyerAndProduct(loggedUserId, orderRequest.getProduct()), OrderResponse.class);
    }
    
    return modelMapper.map(
      orderRepository.save(Order.builder()
        .buyer(loggedUserId)
        .owner(getProductOwnerId(orderRequest.getProduct()))
        .product(orderRequest.getProduct())
        .status(OrderStatus.NEW)
        .address(getAddressId(loggedUserId))
        .paymentMethod(getPaymentMethod(loggedUserId, token))
        .build()), OrderResponse.class);
  }
  
  private void makePayment(Order order) {
    try {
      this.paymentClient.makePayment(order);
    } catch (Exception e) {
      throw new IllegalStateException("Errore nel pagamento");
    }
  }
  
  private void makeRefund(Order order) {
    try {
      this.paymentClient.makeRefund(order);
    } catch (Exception e) {
      throw new IllegalStateException("Errore nel refund del pagamento");
    }
  }
  
  private String getProductOwnerId(String productId) {
    return productClient.getProductById(productId).getOwner();
  }
  
  private String getAddressId(String username) {
    return addressClient.getAddressByUsername(username).getId().toString();
  }
  
  private String getPaymentMethod(String userid, String token) {
    return paymentClient.getPaymentMethodByUserid(token, userid).getId();
  }
  
  private void setProductBoughtStatus(String productId) {
    productClient.setProductBoughtStatus(productId);
  }
  
  @SneakyThrows
  private void sendMessageToOwner(String owner, String buyer, String product) {
    NewOrderEvent newOrderEvent = new NewOrderEvent(product, buyer, owner);
    newOrderEventKafkaTemplate.send("NewOrder", "order-service", newOrderEvent);
  }
}
