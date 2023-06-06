package it.pingflood.winted.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
        .pathMatchers("/eureka/**", "/actuator", "/actuator/**")
        .permitAll()
        .pathMatchers(HttpMethod.GET, "/api/v1/product", "/api/v1/product/**", "/api/v1/product/search")
        .permitAll()
        .pathMatchers(HttpMethod.GET, "/api/v1/resource", "/api/v1/resource/**")
        .permitAll()
        .pathMatchers(HttpMethod.GET, "/api/v1/profile", "/api/v1/profile/**")
        .permitAll()
        .anyExchange()
        .authenticated())
      .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt)
      .build();
  }
}
