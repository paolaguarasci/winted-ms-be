package it.pingflood.winted.messageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;


@SpringBootApplication
@EnableMongoAuditing
public class MessageserviceApplication {
  
  public static void main(String[] args) {
    SpringApplication.run(MessageserviceApplication.class, args);
    
    
  }
  
  
}
