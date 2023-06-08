package it.pingflood.winted.paymentservice.service;

import it.pingflood.winted.paymentservice.data.dto.PaymentMethodRequest;
import it.pingflood.winted.paymentservice.data.dto.PaymentMethodResponse;

import java.util.List;

public interface PaymentMethodService {
  List<PaymentMethodResponse> getByLoggedUser(String principal);
  
  List<PaymentMethodResponse> getByUser(String userid);
  
  PaymentMethodResponse getById(String id, String principal);
  
  PaymentMethodResponse getOneById(String id, String principal);
  
  PaymentMethodResponse createOne(PaymentMethodRequest paymentMethodRequest, String name);
}
