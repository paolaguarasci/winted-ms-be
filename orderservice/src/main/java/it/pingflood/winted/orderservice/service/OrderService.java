package it.pingflood.winted.orderservice.service;

import it.pingflood.winted.orderservice.data.dto.OrderConfirmRequest;
import it.pingflood.winted.orderservice.data.dto.OrderRequest;
import it.pingflood.winted.orderservice.data.dto.OrderResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService {
  OrderResponse getOne(UUID id, String principal);
  
  List<OrderResponse> getAll(String principal);
  
  List<OrderResponse> getAllByUser(String userId, String principal);
  
  OrderResponse confirmOrder(OrderConfirmRequest orderConfirmRequest, String principal, String token);
  
  OrderResponse createPreorder(OrderRequest orderRequest, String principal, String token);
  
}
