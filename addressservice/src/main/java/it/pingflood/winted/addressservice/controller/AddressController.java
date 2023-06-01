package it.pingflood.winted.addressservice.controller;

import it.pingflood.winted.addressservice.data.dto.AddressPUTRequest;
import it.pingflood.winted.addressservice.data.dto.AddressRequest;
import it.pingflood.winted.addressservice.data.dto.AddressResponse;
import it.pingflood.winted.addressservice.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/address")
@Slf4j
@RequiredArgsConstructor
public class AddressController {
  private final AddressService addressService;
  
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<List<AddressResponse>> getAllByUserLogged(Principal principal) {
    return ResponseEntity.ok(addressService.getAllByUserLogged(principal.getName()));
  }
  
  @GetMapping("{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<AddressResponse> getOneById(@PathVariable String id) {
    return ResponseEntity.ok(addressService.getOneById(id));
  }
  
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<AddressResponse> createOne(@RequestBody AddressRequest addressRequest, Principal principal) {
    log.info("Sono qui 1");
    return ResponseEntity.ok(addressService.saveOne(addressRequest, principal));
  }
  
  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<AddressResponse> updateOne(@PathVariable("id") String id, @RequestBody AddressPUTRequest addressPUTRequest, Principal principal) {
    return ResponseEntity.ok(addressService.updateOne(id, addressPUTRequest, principal));
  }
  
  @GetMapping("/user/{userid}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<AddressResponse> getOneByUserid(@PathVariable String userid) {
    return ResponseEntity.ok(addressService.getOneByUserid(userid));
  }
}
