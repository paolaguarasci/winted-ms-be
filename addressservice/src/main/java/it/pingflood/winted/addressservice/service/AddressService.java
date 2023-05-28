package it.pingflood.winted.addressservice.service;

import it.pingflood.winted.addressservice.data.dto.AddressResponse;

public interface AddressService {
  
  AddressResponse getOneByUserLogged();
  
  AddressResponse getOneById(String id);
  
  AddressResponse getOneByUserid(String userid);
}
