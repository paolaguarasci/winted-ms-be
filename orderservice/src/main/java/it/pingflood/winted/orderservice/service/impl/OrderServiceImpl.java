package it.pingflood.winted.orderservice.service.impl;

import it.pingflood.winted.orderservice.data.Order;
import it.pingflood.winted.orderservice.data.dto.OrderPutRequest;
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
    return orderRepository.findAllByUser(userId)
      .stream()
      .map(order -> modelMapper.map(order, OrderResponse.class))
      .toList();
  }
  
  @Override
  public OrderResponse createOrder(OrderRequest orderRequest) {
    if (orderRepository.findByUserAndProduct(orderRequest.getUser(), orderRequest.getProduct()).isPresent()) {
      throw new IllegalArgumentException();
    }
    
    OrderResponse newOrderResponse = modelMapper.map(
      orderRepository.save(Order.builder()
        .user(orderRequest.getUser())
        .product(orderRequest.getProduct())
        .build()), OrderResponse.class);
    
    newOrderEventKafkaTemplate.send("NewOrder", "foo", new NewOrderEvent(newOrderResponse.getProduct(), newOrderResponse.getProduct(), ""));
    return newOrderResponse;
  }
  
  @Override
  public OrderResponse updateOrder(UUID id, OrderPutRequest orderPutRequest) {
    if (!id.equals(orderPutRequest.getId())) {
      throw new IllegalArgumentException();
    }
    Order oldOrder = orderRepository.findById(id).orElseThrow();
    oldOrder.setProduct(orderPutRequest.getProduct());
    return modelMapper.map(orderRepository.save(oldOrder), OrderResponse.class);
  }
  
  @Override
  public void deleteOrder(UUID id) {
    orderRepository.delete(orderRepository.findById(id).orElseThrow());
  }
  
  
}
