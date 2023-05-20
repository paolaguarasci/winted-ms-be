package it.pingflood.winted.addressservice.service.impl;

import it.pingflood.winted.addressservice.data.dto.AddressResponse;
import it.pingflood.winted.addressservice.repository.AddressRepository;
import it.pingflood.winted.addressservice.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class AddressServiceImpl implements AddressService {
  private final ModelMapper modelMapper;
  private final AddressRepository addressRepository;
  
  public AddressServiceImpl(AddressRepository addressRepository) {
    this.addressRepository = addressRepository;
    this.modelMapper = new ModelMapper();
  }
  
  @Override
  public AddressResponse getOneByUserLogged() {
    String userLogged = "paola";
    return modelMapper.map(addressRepository.findFirstByUsername(userLogged).orElseThrow(), AddressResponse.class);
  }
}
