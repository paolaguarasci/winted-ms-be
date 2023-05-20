package it.pingflood.winted.paymentservice.controller;

import it.pingflood.winted.paymentservice.data.dto.PaymentMethodResponse;
import it.pingflood.winted.paymentservice.service.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
}
