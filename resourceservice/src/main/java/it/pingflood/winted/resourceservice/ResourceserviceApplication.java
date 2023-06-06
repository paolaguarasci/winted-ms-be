package it.pingflood.winted.resourceservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info =
@Info(title = "Resource API", version = "1.0", description = "Documentation Resource API v1.0")
)
public class ResourceserviceApplication {
  
  public static void main(String[] args) {
    SpringApplication.run(ResourceserviceApplication.class, args);
  }
  
}
