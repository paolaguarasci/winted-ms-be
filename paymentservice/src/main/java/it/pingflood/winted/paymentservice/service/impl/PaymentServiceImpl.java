package it.pingflood.winted.paymentservice.service.impl;

import it.pingflood.winted.paymentservice.data.PaymentMethod;
import it.pingflood.winted.paymentservice.data.Transaction;
import it.pingflood.winted.paymentservice.data.TransactionStatus;
import it.pingflood.winted.paymentservice.data.dto.TransactionRequest;
import it.pingflood.winted.paymentservice.data.dto.TransactionResponse;
import it.pingflood.winted.paymentservice.repository.PaymentMethodRepository;
import it.pingflood.winted.paymentservice.repository.PaymentRepository;
import it.pingflood.winted.paymentservice.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@Slf4j
public class PaymentServiceImpl implements PaymentService {
  private final PaymentRepository paymentRepository;
  private final PaymentMethodRepository paymentMethodRepository;
  private final ModelMapper modelMapper;
  
  public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentMethodRepository paymentMethodRepository) {
    this.paymentRepository = paymentRepository;
    this.paymentMethodRepository = paymentMethodRepository;
    modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
      .setFieldMatchingEnabled(true)
      .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
  }
  
  @Override
  public TransactionResponse makeOne(TransactionRequest transactionRequest) {
    log.info("TODO Pagamento fatto!");
    if (!checkValidity(transactionRequest)) {
      throw new IllegalArgumentException("Transazione con dati non validi");
    }
    // TODO send message to user
    return makePaymentToSaldo(transactionRequest);
  }
  
  @Override
  public TransactionResponse unlockOne(String id) {
    // TODO Send message to user
    return makePaymentToReal(this.paymentRepository.findById(UUID.fromString(id)).orElseThrow());
  }
  
  @Override
  public TransactionResponse refundOne(String id) {
    log.info("TODO Pagamento refunded!");
    return TransactionResponse.builder().build();
  }
  
  private TransactionResponse makePaymentToSaldo(TransactionRequest transactionRequest) {
    // TODO effettuare il pagamento... api di paypal???
    return modelMapper.map(this.paymentRepository.save(Transaction.builder()
      .paymentTo(transactionRequest.getTo())
      .paymentImport(Double.parseDouble(transactionRequest.getImporto()))
      .paymentMethodId(transactionRequest.getPaymentMethodId())
      .paymentFrom(transactionRequest.getFrom())
      .transactionStatus(TransactionStatus.LOCAL)
      .build()), TransactionResponse.class);
  }
  
  private TransactionResponse makePaymentToReal(Transaction transaction) {
    // TODO effettuare il pagamento... api di paypal???
    Transaction transactionDb = this.paymentRepository.findById(transaction.getId()).orElseThrow();
    transactionDb.setTransactionStatus(TransactionStatus.PAYED);
    paymentRepository.save(transactionDb);
    return modelMapper.map(paymentRepository.save(transactionDb), TransactionResponse.class);
  }
  
  private boolean checkValidity(TransactionRequest transactionRequest) {
    PaymentMethod paymentMethod = paymentMethodRepository.findById(UUID.fromString(transactionRequest.getPaymentMethodId())).orElse(null);
    return paymentMethod != null &&
      transactionRequest.getTo() != null &&
      transactionRequest.getImporto() != null && Double.parseDouble(transactionRequest.getImporto()) > 0 &&
      transactionRequest.getFrom() != null &&
      paymentMethod.getUser().equals(transactionRequest.getFrom());
  }
  
}
