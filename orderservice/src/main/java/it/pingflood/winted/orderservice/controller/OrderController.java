package it.pingflood.winted.orderservice.controller;

import it.pingflood.winted.orderservice.data.dto.OrderPutRequest;
import it.pingflood.winted.orderservice.data.dto.OrderRequest;
import it.pingflood.winted.orderservice.data.dto.OrderResponse;
import it.pingflood.winted.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/order")
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
  public ResponseEntity<OrderResponse> getOne(@PathVariable("id") Long id) {
    return ResponseEntity.of(Optional.of(orderService.getOne(id)));
  }
  
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<OrderResponse> createOne(OrderRequest orderRequest) {
    return ResponseEntity.of(Optional.of(orderService.createOrder(orderRequest)));
  }
  
  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<OrderResponse> updateOne(@PathVariable("id") Long id, OrderPutRequest orderRequest) {
    return ResponseEntity.of(Optional.of(orderService.updateOrder(id, orderRequest)));
  }
  
  
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteOne(@PathVariable("id") Long id) {
    orderService.deleteOrder(id);
  }
}
