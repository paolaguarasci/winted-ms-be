package it.pingflood.winted.productservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pingflood.winted.productservice.data.dto.RolesDTO;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
  private final ObjectMapper objectMapper;
  private final ModelMapper modelMapper;
  
  public SecurityConfig(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    this.modelMapper = new ModelMapper();
    this.modelMapper.getConfiguration()
      .setFieldMatchingEnabled(true)
      .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
  }
  
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
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(jwt -> {
      Collection<GrantedAuthority> authorities = new ArrayList<>();
      try {
        RolesDTO rolesDTO = modelMapper.map(jwt.getClaims().get("realm_access"), RolesDTO.class);
        rolesDTO.getRoles().forEach(role -> {
          authorities.add(new SimpleGrantedAuthority(role));
          authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        });
      } catch (Exception e) {
        log.error("No auth! - {}", e.getMessage());
      }
      return authorities;
    });
    return converter;
  }
}
