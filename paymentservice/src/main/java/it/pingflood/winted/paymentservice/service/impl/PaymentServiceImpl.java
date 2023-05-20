package it.pingflood.winted.paymentservice.service.impl;

import it.pingflood.winted.paymentservice.data.dto.TransactionRequest;
import it.pingflood.winted.paymentservice.data.dto.TransactionResponse;
import it.pingflood.winted.paymentservice.service.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
  @Override
  public TransactionResponse makeOne(TransactionRequest transactionRequest) {
    return null;
  }
}
