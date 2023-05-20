package it.pingflood.winted.orderservice.client;

import it.pingflood.winted.orderservice.client.data.AddressResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("address-service")
public interface AddressClient {
  @GetMapping("/api/v1/address/{id}")
  AddressResponse getAddressById(@PathVariable String id);
  
  @GetMapping("/api/v1/address?username={username}")
  AddressResponse getAddressByUsername(@PathVariable String username);
}
