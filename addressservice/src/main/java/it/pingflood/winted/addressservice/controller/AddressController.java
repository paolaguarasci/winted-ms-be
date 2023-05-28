package it.pingflood.winted.addressservice.controller;

import it.pingflood.winted.addressservice.data.dto.AddressResponse;
import it.pingflood.winted.addressservice.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/address")
@Slf4j
@RequiredArgsConstructor
public class AddressController {
  private final AddressService addressService;
  
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<AddressResponse> getOneByUserLogged() {
    return ResponseEntity.ok(addressService.getOneByUserLogged());
  }
  
  @GetMapping("{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<AddressResponse> getOneById(@PathVariable String id) {
    return ResponseEntity.ok(addressService.getOneById(id));
  }
  
  @GetMapping("/user/{userid}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<AddressResponse> getOneByUserid(@PathVariable String userid) {
    return ResponseEntity.ok(addressService.getOneByUserid(userid));
  }
}
