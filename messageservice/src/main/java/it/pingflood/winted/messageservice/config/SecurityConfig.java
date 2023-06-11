package it.pingflood.winted.messageservice.config;

import it.pingflood.winted.messageservice.data.dto.RolesDTO;
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
  private final ModelMapper modelMapper;
  
  public SecurityConfig() {
    this.modelMapper = new ModelMapper();
    this.modelMapper.getConfiguration()
      .setFieldMatchingEnabled(true)
      .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
  }
  
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.csrf().disable()
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(HttpMethod.GET, "/api/v1/message/v3/api-docs", "/api/v1/message/v3/api-docs/**", "/api/v1/message/webjars/swagger-ui/index.html", "/message/v3/api-docs")
        .permitAll()
        
        // TODO trovare come far funzionare i WebSockets con OAuth2, per ora e' libero...
        // /api/v1/message/websocket/greeting
        .requestMatchers("/api/v1/message/websocket/", "/api/v1/message/websocket/**")
        .permitAll()
        
        
        .requestMatchers("/api/v1/message", "/api/v1/message/**")
        .hasAnyAuthority("ROLE_USER", "USER", "ROLE_ADMIN", "ADMIN")
        .requestMatchers("/api/v1/conversation", "/api/v1/conversation/**")
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
        log.error("No roles! - {}", e.getMessage());
      }
      return authorities;
    });
    return converter;
  }
}
