package it.pingflood.winted.messageservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.config.EnableMongoAuditing;


@SpringBootApplication
@EnableMongoAuditing
@EnableFeignClients
@OpenAPIDefinition(info =
@Info(title = "Message API", version = "1.0", description = "Documentation Message API v1.0")
)
public class MessageserviceApplication {
  
  public static void main(String[] args) {
    SpringApplication.run(MessageserviceApplication.class, args);
    
    
  }
  
  
}
