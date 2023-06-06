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
        
        .pathMatchers(HttpMethod.GET, "/webjars/**").permitAll()
        .pathMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()
        .pathMatchers(HttpMethod.GET, "/swagger-resources/**").permitAll()
        .pathMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
        
        .pathMatchers(HttpMethod.GET, "/product/v3/api-docs").permitAll()
        .pathMatchers(HttpMethod.GET, "/product/v3/api-docs/**").permitAll()
        
        .pathMatchers(HttpMethod.GET, "/message/v3/api-docs").permitAll()
        .pathMatchers(HttpMethod.GET, "/message/v3/api-docs/**").permitAll()
        
        .pathMatchers(HttpMethod.GET, "/address/v3/api-docs").permitAll()
        .pathMatchers(HttpMethod.GET, "/address/v3/api-docs/**").permitAll()
        .pathMatchers(HttpMethod.GET, "/api/v1/address/v3/api-docs", "/api/v1/address/v3/api-docs/**", "/api/v1/address/webjars/swagger-ui/index.html", "/address/v3/api-docs")
        .permitAll()
        .pathMatchers(HttpMethod.GET, "/api/v1/message/v3/api-docs", "/api/v1/message/v3/api-docs/**", "/api/v1/message/webjars/swagger-ui/index.html", "/message/v3/api-docs")
        .permitAll()
        .pathMatchers(HttpMethod.GET, "/order/v3/api-docs").permitAll()
        .pathMatchers(HttpMethod.GET, "/order/v3/api-docs/**").permitAll()
        .pathMatchers(HttpMethod.GET, "/api/v1/order/v3/api-docs", "/api/v1/order/v3/api-docs/**", "/api/v1/order/webjars/swagger-ui/index.html", "/order/v3/api-docs")
        .permitAll()
        
        .pathMatchers(HttpMethod.GET, "/payment/v3/api-docs").permitAll()
        .pathMatchers(HttpMethod.GET, "/payment/v3/api-docs/**").permitAll()
        .pathMatchers(HttpMethod.GET, "/api/v1/payment/v3/api-docs", "/api/v1/payment/v3/api-docs/**", "/api/v1/payment/webjars/swagger-ui/index.html", "/payment/v3/api-docs")
        .permitAll()
        .pathMatchers(HttpMethod.GET, "/profile/v3/api-docs").permitAll()
        .pathMatchers(HttpMethod.GET, "/profile/v3/api-docs/**").permitAll()
        .pathMatchers(HttpMethod.GET, "/api/v1/profile/v3/api-docs", "/api/v1/profile/v3/api-docs/**", "/api/v1/profile/webjars/swagger-ui/index.html", "/profile/v3/api-docs")
        .permitAll()
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
