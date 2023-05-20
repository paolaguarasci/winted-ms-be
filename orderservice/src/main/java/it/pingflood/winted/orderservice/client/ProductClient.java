package it.pingflood.winted.orderservice.client;

import it.pingflood.winted.orderservice.client.data.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("product-service")
public interface ProductClient {
  @GetMapping("/api/v1/product/{id}")
  ProductResponse getProductById(@PathVariable String id);
  
  @PostMapping("/api/v1/product/{id}/bought")
  ProductResponse setProductBoughtStatus(@PathVariable String id);
}
