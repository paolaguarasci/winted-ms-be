package it.pingflood.winted.orderservice.service.impl;

import it.pingflood.winted.orderservice.data.Order;
import it.pingflood.winted.orderservice.data.OrderStatus;
import it.pingflood.winted.orderservice.data.dto.OrderConfirmRequest;
import it.pingflood.winted.orderservice.data.dto.OrderRequest;
import it.pingflood.winted.orderservice.data.dto.OrderResponse;
import it.pingflood.winted.orderservice.event.NewOrderEvent;
import it.pingflood.winted.orderservice.repository.OrderRepository;
import it.pingflood.winted.orderservice.service.OrderService;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
  
  private final OrderRepository orderRepository;
  private final ModelMapper modelMapper;
  
  private final KafkaTemplate<String, NewOrderEvent> newOrderEventKafkaTemplate;
  
  
  public OrderServiceImpl(OrderRepository orderRepository, KafkaTemplate<String, NewOrderEvent> newOrderEventKafkaTemplate) {
    this.orderRepository = orderRepository;
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
    Order order = orderRepository.findById(orderConfirmRequest.getId()).orElseThrow();
    
    // TODO SAGA ???
    
    try {
      makePayment(order);
      order.setStatus(OrderStatus.PAYED);
      newOrderEventKafkaTemplate.send("NewOrder", "order-service", new NewOrderEvent(order.getProduct(), order.getProduct(), ""));
    } catch (Exception e) {
      // makeRefound(order);
      order.setStatus(OrderStatus.PAYMENTERROR);
    }
    return modelMapper.map(order, OrderResponse.class);
  }
  
  @Override
  public OrderResponse createPreorder(OrderRequest orderRequest) {
    String loggedUsername = "paola";
    System.out.println("Prodotto " + orderRequest.getProduct());
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
    // TODO interrogare il servizio PaymentService
  }
  
  private String getProductOwnerId(String productId) {
    // TODO interrogare il servizio ProductService per ottenere l'owner del prodotto
    return "owner12345";
  }
  
  private String getAddressId(String username) {
    // TODO interrogare il servizio AddressService
    return "address12345";
  }
  
  private String getPaymentMethod(String username) {
    // TODO interrogare il servizio PaymentMethod
    return "pyment12345";
  }
}
