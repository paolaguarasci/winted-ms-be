package it.pingflood.winted.orderservice.controller;

import it.pingflood.winted.orderservice.service.OrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
  
  private final OrderService orderService;
  
  public OrderController(OrderService orderService) { this.orderService = orderService; }
}
