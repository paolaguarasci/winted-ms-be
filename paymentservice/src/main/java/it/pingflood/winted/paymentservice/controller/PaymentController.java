package it.pingflood.winted.paymentservice.controller;

import it.pingflood.winted.paymentservice.data.dto.TransactionRequest;
import it.pingflood.winted.paymentservice.data.dto.TransactionResponse;
import it.pingflood.winted.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
  private final PaymentService paymentService;
  
  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<TransactionResponse> makeOne(@RequestBody TransactionRequest transactionRequest) {
    log.info("Make request payment data {}", transactionRequest);
    return ResponseEntity.ok(paymentService.makeOne(transactionRequest));
  }
  
  @PostMapping("/{id}/refund")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<TransactionResponse> refundOne(@PathVariable String id) {
    log.info("Make refound request for payment {}", id);
    return ResponseEntity.ok(paymentService.refundOne(id));
  }
}
