package it.pingflood.winted.addressservice.service.impl;

import it.pingflood.winted.addressservice.data.Address;
import it.pingflood.winted.addressservice.data.dto.AddressPUTRequest;
import it.pingflood.winted.addressservice.data.dto.AddressRequest;
import it.pingflood.winted.addressservice.data.dto.AddressResponse;
import it.pingflood.winted.addressservice.repository.AddressRepository;
import it.pingflood.winted.addressservice.service.AddressService;
import jakarta.ws.rs.NotAuthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
  public List<AddressResponse> getAllByUserLogged(String userId) {
    return addressRepository.findAllByUser(userId).stream().map((element) -> modelMapper.map(element, AddressResponse.class)).collect(Collectors.toList());
  }
  
  @Override
  public AddressResponse getOneById(String uuid) {
    return modelMapper.map(addressRepository.findById(UUID.fromString(uuid)).orElseThrow(), AddressResponse.class);
  }
  
  
  @Override
  public AddressResponse getOneByUserid(String userid) {
    return modelMapper.map(addressRepository.findFirstByUser(userid).orElseThrow(), AddressResponse.class);
  }
  
  @Override
  public AddressResponse saveOne(AddressRequest addressRequest, Principal principal) {
    log.info("Sono qui 2 - principal name {}", principal.getName());
    addressRequest.setUser(principal.getName());
    return modelMapper.map(addressRepository.save(modelMapper.map(addressRequest, Address.class)), AddressResponse.class);
  }
  
  @Override
  public AddressResponse updateOne(String id, AddressPUTRequest addressPUTRequest, Principal principal) {
    if (!id.equals(addressPUTRequest.getId())) {
      throw new IllegalArgumentException("Illegal args");
    }
    if (!addressPUTRequest.getUser().equals(principal.getName())) {
      throw new NotAuthorizedException("Not authorized");
    }
    return modelMapper.map(addressRepository.save(modelMapper.map(addressPUTRequest, Address.class)), AddressResponse.class);
  }
}
