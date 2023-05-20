package it.pingflood.winted.paymentservice.service.impl;

import it.pingflood.winted.paymentservice.data.PaymentMethod;
import it.pingflood.winted.paymentservice.data.dto.PaymentMethodResponse;
import it.pingflood.winted.paymentservice.repository.PaymentMethodRepository;
import it.pingflood.winted.paymentservice.service.PaymentMethodService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class PaymentMethodServiceImpl implements PaymentMethodService {
  private final PaymentMethodRepository paymentMethodRepository;
  private final ModelMapper modelMapper;
  
  public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository) {
    this.paymentMethodRepository = paymentMethodRepository;
    this.modelMapper = new ModelMapper();
  }
  
  @Override
  public PaymentMethodResponse getByLoggedUser() {
    String loggedUsername = "paola";
    PaymentMethod pm = paymentMethodRepository.findByUsername(loggedUsername).orElseThrow();
    int cifreNumeroCarta = pm.getNumeroCarta().length();
    return PaymentMethodResponse.builder()
      .id(pm.getId().toString())
      .last4Digit(pm.getNumeroCarta().substring(cifreNumeroCarta - 4, cifreNumeroCarta))
      .gestore(pm.getGestore())
      .build();
  }
}
