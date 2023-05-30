package it.pingflood.winted.apigateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
public class WebFilter implements org.springframework.web.server.WebFilter {
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    
    String currentUserName = "";
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
      currentUserName = authentication.getName();
    }
    log.info("Utente loggato {}", currentUserName);
    return chain.filter(exchange);
  }
}
