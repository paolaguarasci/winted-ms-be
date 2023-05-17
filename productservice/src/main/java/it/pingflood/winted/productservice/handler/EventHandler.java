package it.pingflood.winted.productservice.handler;

import it.pingflood.winted.productservice.event.ProductEvent;
import it.pingflood.winted.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventHandler {
  
  private final ProductService productService;
  
  @KafkaListener(id = "product-service1", topics = "AddToPreferred")
  public void handleAddPreferred(ProductEvent productEvent) {
    log.info("Nuovo prodotto preferito {}", productEvent.getProductId());
    productService.addPreferred(productEvent.getProductId());
  }
  
  @KafkaListener(id = "product-service2", topics = "RemoteToPreferred")
  public void handleDeleteAddPreferred(ProductEvent productEvent) {
    log.info("Prodotto rimosso dai preferiti {}", productEvent.getProductId());
    productService.removePreferred(productEvent.getProductId());
  }
  
}
