package it.pingflood.winted.orderservice.service.impl;

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
  
  
  public OrderServiceImpl(OrderRepository orderRepository, ProductClient productClient, AddressClient addressClient, PaymentClient paymentClient, KafkaTemplate<String, NewOrderEvent> newOrderEventKafkaTemplate) {
    this.orderRepository = orderRepository;
    this.productClient = productClient;
    this.addressClient = addressClient;
    this.paymentClient = paymentClient;
    this.newOrderEventKafkaTemplate = newOrderEventKafkaTemplate;
    modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
      .setFieldMatchingEnabled(true)
      .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
  }
  
  @Override
  public OrderResponse getOne(UUID id) {
    return modelMapper.map(orderRepository.findById(id).orElseThrow(), OrderResponse.class);
  }
  
  @Override
  public List<OrderResponse> getAll() {
    return orderRepository.findAll()
      .stream()
      .map(order -> modelMapper.map(order, OrderResponse.class))
      .toList();
  }
  
  @Override
  public List<OrderResponse> getAllByUser(String userId) {
    return orderRepository.findAllByBuyer(userId)
      .stream()
      .map(order -> modelMapper.map(order, OrderResponse.class))
      .toList();
  }
  
  @Override
  public OrderResponse confirmOrder(OrderConfirmRequest orderConfirmRequest) {
    String loggedUsername = "paola";
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
      
      if (!addressResponse.getUsername().equals(loggedUsername) ||
        !paymentMethodResponse.getUsername().equals(loggedUsername)) {
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
  public OrderResponse createPreorder(OrderRequest orderRequest) {
    String loggedUsername = "paola";
    if (orderRepository.findByBuyerAndProduct(loggedUsername, orderRequest.getProduct()).isPresent()) {
      return modelMapper.map(orderRepository.findByBuyerAndProduct(loggedUsername, orderRequest.getProduct()), OrderResponse.class);
    }
    
    return modelMapper.map(
      orderRepository.save(Order.builder()
        .buyer(loggedUsername)
        .owner(getProductOwnerId(orderRequest.getProduct()))
        .product(orderRequest.getProduct())
        .status(OrderStatus.NEW)
        .address(getAddressId(loggedUsername))
        .paymentMethod(getPaymentMethod(loggedUsername))
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
  
  private String getPaymentMethod(String username) {
    return paymentClient.getPaymentMethodByUsername(username).getId();
  }
  
  private void setProductBoughtStatus(String productId) {
    productClient.setProductBoughtStatus(productId);
  }
  
  private void sendMessageToOwner(String ownerid, String buyerid, String productId) {
    newOrderEventKafkaTemplate.send("NewOrder", "order-service", new NewOrderEvent(productId, buyerid, ownerid));
  }
}
