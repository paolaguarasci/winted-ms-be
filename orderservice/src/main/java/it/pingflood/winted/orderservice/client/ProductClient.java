package it.pingflood.winted.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// @FeignClient(value = "product-service", url = "https://localhost:8443")
@FeignClient(url = "http://product-service:7001", value = "product-service")
public interface ProductClient {
  @RequestMapping(method = RequestMethod.GET, value = "/api/v1/product/{id}")
  String getProductById(@RequestHeader(value = "Authorization", required = true) String authorizationHeader, @PathVariable String id);
  
  @RequestMapping(method = RequestMethod.POST, value = "/api/v1/product/{id}/bought")
  String setProductBoughtStatus(@RequestHeader(value = "Authorization", required = true) String authorizationHeader, @PathVariable String id);
  
  @RequestMapping(method = RequestMethod.POST, value = "/api/v1/product/{id}/bought/undo")
  String undoSetProductBoughtStatus(@RequestHeader(value = "Authorization", required = true) String authorizationHeader, @PathVariable String id);
}
