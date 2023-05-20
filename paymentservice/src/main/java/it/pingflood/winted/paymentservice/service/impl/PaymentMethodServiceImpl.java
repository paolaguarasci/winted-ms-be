package it.pingflood.winted.paymentservice.service.impl;

import it.pingflood.winted.paymentservice.data.PaymentMethod;
import it.pingflood.winted.paymentservice.data.dto.PaymentMethodResponse;
import it.pingflood.winted.paymentservice.repository.PaymentMethodRepository;
import it.pingflood.winted.paymentservice.service.PaymentMethodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@Transactional
public class PaymentMethodServiceImpl implements PaymentMethodService {
  private final PaymentMethodRepository paymentMethodRepository;
  
  public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository) {
    this.paymentMethodRepository = paymentMethodRepository;
  }
  
  @Override
  public PaymentMethodResponse getByLoggedUser() {
    String loggedUsername = "paola";
    return getObfuscate(paymentMethodRepository.findByUsername(loggedUsername).orElseThrow());
  }
  
  @Override
  public PaymentMethodResponse getByUsername(String username) {
    return getObfuscate(paymentMethodRepository.findByUsername(username).orElseThrow());
  }
  
  @Override
  public PaymentMethodResponse getById(String id) {
    return getObfuscate(paymentMethodRepository.findById(UUID.fromString(id)).orElseThrow());
  }
  
  private PaymentMethodResponse getObfuscate(PaymentMethod pm) {
    int cifreNumeroCarta = pm.getNumeroCarta().length();
    return PaymentMethodResponse.builder()
      .id(pm.getId().toString())
      .last4Digit(pm.getNumeroCarta().substring(cifreNumeroCarta - 4, cifreNumeroCarta))
      .gestore(pm.getGestore())
      .build();
  }
}
