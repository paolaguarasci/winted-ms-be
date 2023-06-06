package it.pingflood.winted.profileservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
@OpenAPIDefinition(info =
@Info(title = "Profile API", version = "1.0", description = "Documentation Profile API v1.0")
)
public class ProfileserviceApplication {
  
  public static void main(String[] args) {
    SpringApplication.run(ProfileserviceApplication.class, args);
  }
  
}
