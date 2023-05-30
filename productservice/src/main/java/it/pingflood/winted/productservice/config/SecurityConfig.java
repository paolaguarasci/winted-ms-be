package it.pingflood.winted.productservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;

@Configuration
@Slf4j
public class SecurityConfig {
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.csrf().disable()
      .authorizeRequests(auth -> auth
        .requestMatchers(HttpMethod.GET, "/api/v1/product", "/api/v1/product/**")
        .permitAll()
        .requestMatchers(HttpMethod.POST, "/api/v1/product", "/api/v1/product/**")
        .hasAnyAuthority("ROLE_USER", "USER", "ROLE_ADMIN", "ADMIN")
        .anyRequest()
        .authenticated())
      .oauth2ResourceServer(oauth2 -> oauth2.jwt().jwtAuthenticationConverter(jwtGrantedAuthoritiesConverter()))
      .build();
  }
  
  public Converter<Jwt, AbstractAuthenticationToken> jwtGrantedAuthoritiesConverter() {
    log.info("Converter 1");
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(jwt -> {
      log.info("Converter 2 - {} ", jwt.getClaims().get("realm_access"));
      Collection<GrantedAuthority> authorities = new ArrayList<>();
      authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
      authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
      authorities.add(new SimpleGrantedAuthority("USER"));
      authorities.add(new SimpleGrantedAuthority("ADMIN"));
      return authorities;
    });
    return converter;
  }
}
