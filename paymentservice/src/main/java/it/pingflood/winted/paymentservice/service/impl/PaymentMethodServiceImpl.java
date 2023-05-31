package it.pingflood.winted.paymentservice.service.impl;

import it.pingflood.winted.paymentservice.data.PaymentMethod;
import it.pingflood.winted.paymentservice.data.dto.PaymentMethodResponse;
import it.pingflood.winted.paymentservice.repository.PaymentMethodRepository;
import it.pingflood.winted.paymentservice.service.PaymentMethodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
  public PaymentMethodResponse getByLoggedUser(String principal) {
    return getObfuscate(paymentMethodRepository.findByUser(principal).orElseThrow());
  }
  
  @Override
  public PaymentMethodResponse getByUser(String userid) {
    return getObfuscate(paymentMethodRepository.findByUser(userid).orElseThrow());
  }
  
  @Override
  public PaymentMethodResponse getById(String id, String principal) {
    PaymentMethod pm = paymentMethodRepository.findById(UUID.fromString(id)).orElseThrow();
    if (!pm.getUser().equals(principal)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }
    return getObfuscate(pm);
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
