package it.pingflood.winted.addressservice.service;

import it.pingflood.winted.addressservice.data.dto.AddressPUTRequest;
import it.pingflood.winted.addressservice.data.dto.AddressRequest;
import it.pingflood.winted.addressservice.data.dto.AddressResponse;

import java.security.Principal;
import java.util.List;

public interface AddressService {
  
  List<AddressResponse> getAllByUserLogged(String userid);
  
  AddressResponse getOneById(String id);
  
  AddressResponse getOneByUserid(String userid);
  
  AddressResponse saveOne(AddressRequest addressRequest, Principal principal);
  
  AddressResponse updateOne(String id, AddressPUTRequest addressPUTRequest, Principal principal);
}
