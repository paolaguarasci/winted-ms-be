package it.pingflood.winted.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class ProductserviceApplication {
  
  public static void main(String[] args) {
    SpringApplication.run(ProductserviceApplication.class, args);
  }
  
  
}
