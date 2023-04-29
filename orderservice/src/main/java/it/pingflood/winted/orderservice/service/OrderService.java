package it.pingflood.winted.orderservice.service;

import it.pingflood.winted.orderservice.data.dto.OrderPutRequest;
import it.pingflood.winted.orderservice.data.dto.OrderRequest;
import it.pingflood.winted.orderservice.data.dto.OrderResponse;

import java.util.List;

public interface OrderService {
  OrderResponse getOne(Long id);
  
  List<OrderResponse> getAll();
  
  List<OrderResponse> getAllByUser(String userId);
  
  OrderResponse createOrder(OrderRequest orderRequest);
  
  OrderResponse updateOrder(Long id, OrderPutRequest orderPutRequest);
  
  void deleteOrder(Long id);
}
