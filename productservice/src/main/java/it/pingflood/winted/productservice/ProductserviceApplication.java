package it.pingflood.winted.productservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
@OpenAPIDefinition(info =
@Info(title = "Course API", version = "1.0", description = "Documentation Course API v1.0")
)
public class ProductserviceApplication {
  
  public static void main(String[] args) {
    SpringApplication.run(ProductserviceApplication.class, args);
  }
  
  
}
