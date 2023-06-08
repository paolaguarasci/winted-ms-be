package it.pingflood.winted.paymentservice.controller;

import it.pingflood.winted.paymentservice.data.dto.PaymentMethodRequest;
import it.pingflood.winted.paymentservice.data.dto.PaymentMethodResponse;
import it.pingflood.winted.paymentservice.service.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/paymentmethod")
@RequiredArgsConstructor
public class PaymentMethodController {
  private final PaymentMethodService paymentMethodService;
  
  
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<List<PaymentMethodResponse>> getAll(Principal principal) {
    return ResponseEntity.ok(paymentMethodService.getByLoggedUser(principal.getName()));
  }
  
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<PaymentMethodResponse> createOne(@RequestBody PaymentMethodRequest paymentMethodRequest, Principal principal) {
    return ResponseEntity.ok(paymentMethodService.createOne(paymentMethodRequest, principal.getName()));
  }
  
  @GetMapping("{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<PaymentMethodResponse> getOneById(@PathVariable String id, Principal principal) {
    return ResponseEntity.ok(paymentMethodService.getById(id, principal.getName()));
  }
  
  @GetMapping("/user/{userid}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<PaymentMethodResponse> getOneByUser(@PathVariable String userid, Principal principal) {
    return ResponseEntity.ok(paymentMethodService.getOneById(userid, principal.getName()));
  }
}
