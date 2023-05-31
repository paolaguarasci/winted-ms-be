package it.pingflood.winted.paymentservice.service;

import it.pingflood.winted.paymentservice.data.dto.PaymentMethodResponse;

public interface PaymentMethodService {
  PaymentMethodResponse getByLoggedUser(String principal);
  
  PaymentMethodResponse getByUser(String userid);
  
  PaymentMethodResponse getById(String id, String principal);
}
