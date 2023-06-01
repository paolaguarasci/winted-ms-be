package it.pingflood.winted.profileservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class ProfileserviceApplication {
  
  public static void main(String[] args) {
    SpringApplication.run(ProfileserviceApplication.class, args);
  }
  
}
