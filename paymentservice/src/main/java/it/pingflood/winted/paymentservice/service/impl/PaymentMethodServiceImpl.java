package it.pingflood.winted.paymentservice.service.impl;

import it.pingflood.winted.paymentservice.data.PaymentMethod;
import it.pingflood.winted.paymentservice.data.dto.PaymentMethodRequest;
import it.pingflood.winted.paymentservice.data.dto.PaymentMethodResponse;
import it.pingflood.winted.paymentservice.repository.PaymentMethodRepository;
import it.pingflood.winted.paymentservice.service.PaymentMethodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class PaymentMethodServiceImpl implements PaymentMethodService {
  private final PaymentMethodRepository paymentMethodRepository;
  
  public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository) {
    this.paymentMethodRepository = paymentMethodRepository;
  }
  
  @Override
  public List<PaymentMethodResponse> getByLoggedUser(String principal) {
    return paymentMethodRepository.findByUser(principal).stream().map(this::getObfuscate).collect(Collectors.toList());
  }
  
  @Override
  public List<PaymentMethodResponse> getByUser(String userid) {
    return paymentMethodRepository.findByUser(userid).stream().map(this::getObfuscate).collect(Collectors.toList());
  }
  
  @Override
  public PaymentMethodResponse getById(String id, String principal) {
    PaymentMethod pm = paymentMethodRepository.findById(UUID.fromString(id)).orElseThrow();
    if (!pm.getUser().equals(principal)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }
    return getObfuscate(pm);
  }
  
  @Override
  public PaymentMethodResponse getOneById(String id, String principal) {
    PaymentMethod pm = paymentMethodRepository.findFirstByUser(principal).orElseThrow();
    if (!pm.getUser().equals(principal)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }
    return getObfuscate(pm);
  }
  
  @Override
  public PaymentMethodResponse createOne(PaymentMethodRequest paymentMethodRequest, String principal) {
    PaymentMethod paymentMethod = PaymentMethod.builder()
      .gestore(paymentMethodRequest.getGestore())
      .ccv(paymentMethodRequest.getCcv())
      .dataScadenza(paymentMethodRequest.getDataScadenza())
      .numeroCarta(paymentMethodRequest.getNumeroCarta())
      .titolareCarta(paymentMethodRequest.getTitolareCarta())
      .user(principal)
      .build();
    return getObfuscate(paymentMethodRepository.save(paymentMethod));
  }
  
  private PaymentMethodResponse getObfuscate(PaymentMethod pm) {
    int cifreNumeroCarta = pm.getNumeroCarta().length();
    return PaymentMethodResponse.builder()
      .id(pm.getId().toString())
      .user(pm.getUser())
      .last4Digit(pm.getNumeroCarta().substring(cifreNumeroCarta - 4, cifreNumeroCarta))
      .gestore(pm.getGestore())
      .build();
  }
}
