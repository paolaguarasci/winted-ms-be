package it.pingflood.winted.paymentservice.config;

import it.pingflood.winted.paymentservice.data.PaymentMethod;
import it.pingflood.winted.paymentservice.repository.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitDB implements CommandLineRunner {
  private final PaymentMethodRepository paymentMethodRepository;
  
  @Override
  public void run(String... args) throws Exception {
    log.info("InitDB");
    paymentMethodRepository.save(PaymentMethod.builder()
      .user("6464d3155ded8d052d323c2a")
      .gestore("visa")
      .titolareCarta("Giovanni Rossi")
      .numeroCarta("1111222233334444")
      .ccv("123")
      .dataScadenza("12/24")
      .build());
    log.info("Fine init db");
  }
}
