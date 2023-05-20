package it.pingflood.winted.orderservice.controller;

import it.pingflood.winted.orderservice.data.dto.OrderConfirmRequest;
import it.pingflood.winted.orderservice.data.dto.OrderRequest;
import it.pingflood.winted.orderservice.data.dto.OrderResponse;
import it.pingflood.winted.orderservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@Slf4j
public class OrderController {
  
  private final OrderService orderService;
  
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }
  
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<List<OrderResponse>> getAll() {
    // FIXME Ha senso fare l'optional di una lista?
    return ResponseEntity.of(Optional.of(orderService.getAll()));
  }
  
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<OrderResponse> getOne(@PathVariable("id") UUID id) {
    return ResponseEntity.of(Optional.of(orderService.getOne(id)));
  }
  
  @PostMapping("/confirm")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<OrderResponse> confirmOrder(OrderConfirmRequest orderConfirmRequest) {
    return ResponseEntity.of(Optional.of(orderService.confirmOrder(orderConfirmRequest)));
  }
  
  @PostMapping("/checkout")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<OrderResponse> createPreorder(@RequestBody OrderRequest orderRequest) {
    System.out.println(orderRequest);
    return ResponseEntity.of(Optional.of(orderService.createPreorder(orderRequest)));
  }
}
