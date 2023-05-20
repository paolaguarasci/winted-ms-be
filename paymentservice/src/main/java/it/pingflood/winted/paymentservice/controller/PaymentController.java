package it.pingflood.winted.paymentservice.controller;

import it.pingflood.winted.paymentservice.data.dto.TransactionRequest;
import it.pingflood.winted.paymentservice.data.dto.TransactionResponse;
import it.pingflood.winted.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
  private final PaymentService paymentService;
  
  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<TransactionResponse> makeOne(@RequestBody TransactionRequest transactionRequest) {
    return ResponseEntity.ok(paymentService.makeOne(transactionRequest));
  }
  
}
