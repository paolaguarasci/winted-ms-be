package it.pingflood.winted.orderservice.service;

import it.pingflood.winted.orderservice.data.dto.OrderConfirmRequest;
import it.pingflood.winted.orderservice.data.dto.OrderRequest;
import it.pingflood.winted.orderservice.data.dto.OrderResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService {
  OrderResponse getOne(UUID id);
  
  List<OrderResponse> getAll();
  
  List<OrderResponse> getAllByUser(String userId);
  
  OrderResponse confirmOrder(OrderConfirmRequest orderConfirmRequest);
  
  OrderResponse createPreorder(OrderRequest orderRequest);
  
}
