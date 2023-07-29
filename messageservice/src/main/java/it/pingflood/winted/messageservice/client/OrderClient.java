package it.pingflood.winted.messageservice.client;

import it.pingflood.winted.messageservice.client.dto.NewCheckOutRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// @FeignClient(url = "https://localhost:8443", name = "order-service")
@FeignClient(name = "order-service")
public interface OrderClient {
  @RequestMapping(method = RequestMethod.POST, value = "/api/v1/order/checkout/service")
  String postNewCheckout(@RequestHeader(value = "Authorization", required = true) String authorizationHeader, NewCheckOutRequest newCheckOutRequest);
}
