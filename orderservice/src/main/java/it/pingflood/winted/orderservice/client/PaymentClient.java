package it.pingflood.winted.orderservice.client;

import it.pingflood.winted.orderservice.client.data.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("payment-service")
public interface PaymentClient {
  @RequestMapping(method = RequestMethod.GET, value = "/api/v1/paymentmethod/{id}")
  String getPaymentMethodById(@RequestHeader(value = "Authorization", required = true) String authorizationHeader, @PathVariable String id);
  
  @RequestMapping(method = RequestMethod.GET, value = "/api/v1/paymentmethod?username={username}")
  String getPaymentMethodByUsername(@RequestHeader(value = "Authorization", required = true) String authorizationHeader, @PathVariable String username);
  
  @RequestMapping(method = RequestMethod.GET, value = "/api/v1/paymentmethod/user/{userid}")
  String getPaymentMethodByUserid(@RequestHeader(value = "Authorization", required = true) String authorizationHeader, @PathVariable String userid);
  
  @GetMapping("/api/v1/payment/{id}")
  String getPaymentById(@RequestHeader(value = "Authorization", required = true) String authorizationHeader, @PathVariable String id);
  
  @RequestMapping(method = RequestMethod.POST, value = "/api/v1/payment")
  String makePayment(@RequestHeader(value = "Authorization", required = true) String authorizationHeader, @RequestBody PaymentRequest paymentRequest);
  
  @RequestMapping(method = RequestMethod.POST, value = "/api/v1/payment/{id}/refund")
  String makeRefund(@RequestHeader(value = "Authorization", required = true) String authorizationHeader, @PathVariable String id);
}
