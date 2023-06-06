package it.pingflood.winted.paymentservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@OpenAPIDefinition(info =
@Info(title = "Payment API", version = "1.0", description = "Documentation Payment API v1.0")
)
public class PaymentserviceApplication {
  
  public static void main(String[] args) {
    SpringApplication.run(PaymentserviceApplication.class, args);
  }
  
}
