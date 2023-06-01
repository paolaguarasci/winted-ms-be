package it.pingflood.winted.profileservice.config;

import it.pingflood.winted.profileservice.data.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class PersistenceConfig {
  @Bean
  AuditorAware<String> auditorProvider() {
    return new AuditorAwareImpl();
  }
}
