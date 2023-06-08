package it.pingflood.winted.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(url = "https://localhost:8443", name = "address-service")
public interface AddressClient {
  @RequestMapping(method = RequestMethod.GET, value = "/api/v1/address/{id}")
  String getAddressById(@RequestHeader(value = "Authorization", required = true) String authorizationHeader, @PathVariable(value = "id") String id);
  
  
  @RequestMapping(method = RequestMethod.GET, value = "/api/v1/address/user/{userid}")
  String getAddressByUserId(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                            @PathVariable(value = "userid") String userid);
}
