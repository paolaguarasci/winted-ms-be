package it.pingflood.winted.orderservice.service.impl;

import it.pingflood.winted.orderservice.data.Order;
import it.pingflood.winted.orderservice.data.dto.OrderPutRequest;
import it.pingflood.winted.orderservice.data.dto.OrderRequest;
import it.pingflood.winted.orderservice.data.dto.OrderResponse;
import it.pingflood.winted.orderservice.repository.OrderRepository;
import it.pingflood.winted.orderservice.service.OrderService;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
  
  private final OrderRepository orderRepository;
  private final ModelMapper modelMapper;
  
  public OrderServiceImpl(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
    modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
      .setFieldMatchingEnabled(true)
      .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
  }
  
  @Override
  public OrderResponse getOne(Long id) {
    return modelMapper.map(orderRepository.findById(id).orElseThrow(), OrderResponse.class);
  }
  
  @Override
  public List<OrderResponse> getAll() {
    return orderRepository.findAll()
      .stream()
      .map(order -> modelMapper.map(order, OrderResponse.class))
      .collect(Collectors.toList());
  }
  
  @Override
  public List<OrderResponse> getAllByUser(String userId) {
    return orderRepository.findAllByUser(userId)
      .stream()
      .map(order -> modelMapper.map(order, OrderResponse.class))
      .collect(Collectors.toList());
  }
  
  @Override
  public OrderResponse createOrder(OrderRequest orderRequest) {
    return modelMapper.map(
      orderRepository.save(Order.builder()
        .user(orderRequest.getUser())
        .product(orderRequest.getProduct())
        .build()), OrderResponse.class);
  }
  
  @Override
  public OrderResponse updateOrder(Long id, OrderPutRequest orderPutRequest) {
    Order oldOrder = orderRepository.findById(id).orElseThrow();
    oldOrder.setProduct(orderPutRequest.getProduct());
    return modelMapper.map(orderRepository.save(oldOrder), OrderResponse.class);
  }
  
  @Override
  public void deleteOrder(Long id) {
    orderRepository.delete(orderRepository.findById(id).orElseThrow());
  }
  
  
}
