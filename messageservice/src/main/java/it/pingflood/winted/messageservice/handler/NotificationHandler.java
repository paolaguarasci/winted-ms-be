package it.pingflood.winted.messageservice.handler;

import it.pingflood.winted.messageservice.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationHandler {
  @KafkaListener(id = "message-service1", topics = "NewProduct")
  public void handleNewTopic(NewProductEvent newProductEvent) {
    log.info("Nuovo prodotto caricato {}", newProductEvent.getProductId());
  }
  
  @KafkaListener(id = "message-service2", topics = "NewOrder")
  public void handleNewOrder(NewOrderEvent newOrderEvent) {
    log.info("L'utente {} ha comprato da {} il prodotto {}", newOrderEvent.getBuyerId(), newOrderEvent.getSellerId(),
      newOrderEvent.getProductId());
  }
  
  @KafkaListener(id = "message-service3", topics = "NewReplay")
  public void handleNewReplay(NewReplayEvent newReplayEvent) {
    log.info("L'utente {} ha risposto all'utente {}", newReplayEvent.getActor1Id(), newReplayEvent.getActor2Id());
  }
  
  @KafkaListener(id = "message-service4", topics = "NewOffer")
  public void handleNewOffer(NewOfferEvent newOfferEvent) {
    log.info("L'utente {} ha fatto un'offerta all'utente {} per il prodotto {} al prezzo di {}",
      newOfferEvent.getSellerId(), newOfferEvent.getBuyerId(), newOfferEvent.getProductId(),
      newOfferEvent.getPrice());
  }
  
  @KafkaListener(id = "message-service", topics = "OfferAccepted")
  public void handleOfferAccepted(OfferAcceptedEvent offerAcceptedEvent) {
    if (offerAcceptedEvent.getIsAccepted()) {
      log.info("L'utente {} ha accettato l'offerta dell'utente {} per il prodotto {} al prezzo di {}",
        offerAcceptedEvent.getBuyerId(), offerAcceptedEvent.getSellerId(), offerAcceptedEvent.getProductId(),
        offerAcceptedEvent.getPrice());
    } else {
      log.info("L'utente {} non ha accettato l'offerta dell'utente {} per il prodotto {} al prezzo di {}",
        offerAcceptedEvent.getBuyerId(), offerAcceptedEvent.getSellerId(), offerAcceptedEvent.getProductId(),
        offerAcceptedEvent.getPrice());
    }
  }
}
