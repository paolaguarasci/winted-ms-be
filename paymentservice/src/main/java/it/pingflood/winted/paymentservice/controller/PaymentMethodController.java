package it.pingflood.winted.paymentservice.controller;

import it.pingflood.winted.paymentservice.data.dto.PaymentMethodResponse;
import it.pingflood.winted.paymentservice.service.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/paymentmethod")
@RequiredArgsConstructor
public class PaymentMethodController {
  private final PaymentMethodService paymentMethodService;
  
  
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<PaymentMethodResponse> getAll() {
    return ResponseEntity.ok(paymentMethodService.getByLoggedUser());
  }
  
  
  @GetMapping("{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<PaymentMethodResponse> getOneById(@PathVariable String id) {
    return ResponseEntity.ok(paymentMethodService.getById(id));
  }
  
  @GetMapping("/username/{username}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<PaymentMethodResponse> getOneByUsername(@PathVariable String username) {
    return ResponseEntity.ok(paymentMethodService.getByUsername(username));
  }
}
