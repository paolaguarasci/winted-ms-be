package it.pingflood.winted.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    return http
      .csrf().disable()
      .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
        .pathMatchers("/eureka/**")
        .permitAll()
        .pathMatchers("/api/v1/product/**")
        .permitAll()
        .pathMatchers("/api/v1/resource/**")
        .permitAll()
        .pathMatchers("/api/v1/profile/**")
        .permitAll()
        .pathMatchers("/api/v1/address", "/api/v1/address/**")
        .permitAll()
        .pathMatchers("/api/v1/payment", "/api/v1/payment/**")
        .permitAll()
        .pathMatchers("/api/v1/paymentmethod", "/api/v1/paymentmethod/**")
        .permitAll()
        .pathMatchers("/api/v1/order/**")
        .permitAll()
        .anyExchange()
        .authenticated())
      .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt)
      .build();
  }
}
