package it.pingflood.winted.messageservice.config;

import it.pingflood.winted.messageservice.data.AuditorAwareImpl;
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
