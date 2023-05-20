package it.pingflood.winted.paymentservice.service.impl;

import it.pingflood.winted.paymentservice.data.dto.PaymentMethodResponse;
import it.pingflood.winted.paymentservice.service.PaymentMethodService;
import org.springframework.stereotype.Service;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {
  @Override
  public PaymentMethodResponse getByLoggedUser() {
    return null;
  }
}
