package it.pingflood.winted.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OrderserviceApplication {
  
  public static void main(String[] args) {
    SpringApplication.run(OrderserviceApplication.class, args);
  }
  
}
