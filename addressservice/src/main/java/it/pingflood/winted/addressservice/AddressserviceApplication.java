package it.pingflood.winted.addressservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AddressserviceApplication {
  
  public static void main(String[] args) {
    SpringApplication.run(AddressserviceApplication.class, args);
  }
  
}
