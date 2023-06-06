package it.pingflood.winted.orderservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@OpenAPIDefinition(info =
@Info(title = "Order API", version = "1.0", description = "Documentation Order API v1.0")
)
public class OrderserviceApplication {
  
  public static void main(String[] args) {
    SpringApplication.run(OrderserviceApplication.class, args);
  }
  
}
