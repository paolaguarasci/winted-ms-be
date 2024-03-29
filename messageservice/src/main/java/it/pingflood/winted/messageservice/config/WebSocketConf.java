package it.pingflood.winted.messageservice.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConf implements WebSocketMessageBrokerConfigurer {
  
  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/room", "/notify");
    config.setApplicationDestinationPrefixes("/api/v1/message");
//    config.setUserDestinationPrefix("/user");
  }
  
  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/api/v1/message/websocket").setAllowedOriginPatterns("https://localhost:4200").withSockJS();
    registry.addEndpoint("/api/v1/message/websocket").setAllowedOriginPatterns("https://localhost:4200");
  }
  
}
