package it.pingflood.winted.productservice.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pingflood.winted.productservice.event.GenericEvent;
import it.pingflood.winted.productservice.event.ProductEvent;
import it.pingflood.winted.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventHandler {
  private final ObjectMapper objectMapper;
  private final ProductService productService;
  
  @SneakyThrows
  @KafkaListener(id = "product-service1", topics = "AddToPreferred")
  public void handleAddPreferred(@Payload GenericEvent genericEvent, @Headers Map headers) {
    ProductEvent productEvent = objectMapper.readValue(genericEvent.getPayload(), ProductEvent.class);
    log.info("Nuovo prodotto preferito {}", productEvent.getProductId());
    productService.addPreferred(productEvent.getProductId());
  }
  
  @SneakyThrows
  @KafkaListener(id = "product-service2", topics = "RemoteToPreferred")
  public void handleDeleteAddPreferred(@Payload GenericEvent genericEvent, @Headers Map headers) {
    ProductEvent productEvent = objectMapper.readValue(genericEvent.getPayload(), ProductEvent.class);
    log.info("Prodotto rimosso dai preferiti {}", productEvent.getProductId());
    productService.removePreferred(productEvent.getProductId());
  }
  
}
