package it.pingflood.winted.orderservice.controller;

import it.pingflood.winted.orderservice.data.dto.*;
import it.pingflood.winted.orderservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
  public ResponseEntity<List<OrderResponse>> getAll(Principal principal) {
    return ResponseEntity.ok(orderService.getAll(principal.getName()));
  }
  
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<OrderResponse> getOne(@PathVariable("id") UUID id, Principal principal) {
    return ResponseEntity.of(Optional.of(orderService.getOne(id, principal.getName())));
  }
  
  @PostMapping("/confirm")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<OrderResponse> confirmOrder(@RequestBody OrderConfirmRequest orderConfirmRequest, Principal principal, @AuthenticationPrincipal Jwt token) {
    return ResponseEntity.of(Optional.of(orderService.confirmOrder(orderConfirmRequest, principal.getName(), token.getTokenValue())));
  }
  
  @PostMapping("/checkout")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<OrderResponse> createPreorder(@RequestBody OrderRequest orderRequest, Principal principal, @AuthenticationPrincipal Jwt token) {
    log.debug("{}", orderRequest);
    return ResponseEntity.of(Optional.of(orderService.createPreorder(orderRequest, principal.getName(), token.getTokenValue())));
  }
  
  @PostMapping("/checkout/service")
//  @PreAuthorize("hasAuthority('ADMIN')")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<OrderResponse> createPreorderByService(@RequestBody OrderRequestService orderRequestByService, Principal principal, @AuthenticationPrincipal Jwt token) {
    log.debug("{}", orderRequestByService);
    return ResponseEntity.of(Optional.of(orderService.createPreorderService(orderRequestByService, principal.getName(), token.getTokenValue())));
  }
  
  @PutMapping("/checkout/{id}")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<OrderResponse> updatePreorder(@PathVariable("id") UUID id, @RequestBody OrderUpdateRequest orderUpdateRequest, Principal principal, @AuthenticationPrincipal Jwt token) {
    log.debug("==================================================>>>>>>>>>>>>>>> PUT {}", orderUpdateRequest);
    return ResponseEntity.of(Optional.of(orderService.updatePreorder(id, orderUpdateRequest, principal.getName(), token.getTokenValue())));
  }
}
