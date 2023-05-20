package it.pingflood.winted.paymentservice.service;

import it.pingflood.winted.paymentservice.data.dto.PaymentMethodResponse;

public interface PaymentMethodService {
  PaymentMethodResponse getByLoggedUser();
  
  PaymentMethodResponse getByUsername(String username);
  
  PaymentMethodResponse getById(String id);
}
