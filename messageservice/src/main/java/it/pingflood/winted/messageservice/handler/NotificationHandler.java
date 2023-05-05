package it.pingflood.winted.messageservice.handler;

import it.pingflood.winted.messageservice.event.NewProductEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationHandler {
  
  @KafkaListener(id = "foo", topics = "NewProduct")
  public void handleNotificationTopic(NewProductEvent newProductEvent) {
    log.info("Ricevuta notifica su prodotto caricato #{}", newProductEvent.getProductId());
  }
}
