package it.pingflood.winted.orderservice.client;

import feign.Headers;
import feign.Param;
import it.pingflood.winted.orderservice.client.data.PaymentMethodResponse;
import it.pingflood.winted.orderservice.client.data.PaymentResponse;
import it.pingflood.winted.orderservice.data.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("payment-service")
public interface PaymentClient {
  @GetMapping("/api/v1/paymentmethod/{id}")
  PaymentMethodResponse getPaymentMethodById(@PathVariable String id);
  
  @GetMapping("/api/v1/paymentmethod?username={username}")
  PaymentMethodResponse getPaymentMethodByUsername(@PathVariable String username);
  
  @GetMapping("/api/v1/paymentmethod/user/{userid}")
  @Headers("Authorization: Bearer {token}")
  PaymentMethodResponse getPaymentMethodByUserid(@Param("token") String token, @PathVariable String userid);
  
  @GetMapping("/api/v1/payment/{id}")
  PaymentResponse getPaymentById(@PathVariable String id);
  
  @PostMapping("/api/v1/payment")
  PaymentResponse makePayment(@RequestBody Order order);
  
  @PostMapping("/api/v1/payment/{id}/refund")
  PaymentResponse makeRefund(@RequestBody Order order);
}
