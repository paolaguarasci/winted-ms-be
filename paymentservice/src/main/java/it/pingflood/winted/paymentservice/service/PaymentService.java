package it.pingflood.winted.paymentservice.service;

import it.pingflood.winted.paymentservice.data.dto.TransactionRequest;
import it.pingflood.winted.paymentservice.data.dto.TransactionResponse;

public interface PaymentService {
  TransactionResponse makeOne(TransactionRequest transactionRequest);
  
  TransactionResponse unlockOne(String id);
  
  TransactionResponse refundOne(String id);
}
