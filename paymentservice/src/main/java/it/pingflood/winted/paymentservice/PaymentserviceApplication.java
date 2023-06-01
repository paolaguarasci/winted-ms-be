package it.pingflood.winted.paymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class PaymentserviceApplication {
  
  public static void main(String[] args) {
    SpringApplication.run(PaymentserviceApplication.class, args);
  }
  
}
