package it.pingflood.winted.discoveryserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Value("${eureka.username}")
  private String username;
  
  @Value("${eureka.password}")
  private String password;
  
  @Bean
  public InMemoryUserDetailsManager userDetailsService() {
    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    String passEncoded = encoder.encode(password);
    UserDetails user = User.withUsername(username)
      .password(passEncoded)
      .roles("USER")
      .build();
    return new InMemoryUserDetailsManager(user);
  }
  
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
      .csrf().disable()
      .authorizeHttpRequests().anyRequest()
      .authenticated()
      .and()
      .httpBasic()
      .and().build();
  }
}
