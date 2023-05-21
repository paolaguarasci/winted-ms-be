package it.pingflood.winted.messageservice.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pingflood.winted.messageservice.data.MsgType;
import it.pingflood.winted.messageservice.data.dto.ConversationRequest;
import it.pingflood.winted.messageservice.data.dto.ConversationResponse;
import it.pingflood.winted.messageservice.data.dto.MessageRequest;
import it.pingflood.winted.messageservice.event.GenericEvent;
import it.pingflood.winted.messageservice.event.NewOrderEvent;
import it.pingflood.winted.messageservice.event.NewProductEvent;
import it.pingflood.winted.messageservice.service.ConversationService;
import it.pingflood.winted.messageservice.service.MessageService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@Slf4j
public class NotificationHandler {
  
  private final MessageService messageService;
  private final ConversationService conversationService;
  private final ModelMapper modelMapper;
  private final ObjectMapper objectMapper;
  
  public NotificationHandler(MessageService messageService, ConversationService conversationService, ObjectMapper objectMapper) {
    this.messageService = messageService;
    this.conversationService = conversationService;
    this.objectMapper = objectMapper;
    this.modelMapper = new ModelMapper();
  }
  
  @KafkaListener(id = "message-service1", topics = "NewProduct")
  @SneakyThrows
  public void handleNewTopic(@Payload GenericEvent genericEvent, @Headers Map headers) {
    NewProductEvent newProductEvent = objectMapper.readValue(genericEvent.getPayload(), NewProductEvent.class);
    log.info("Nuovo prodotto caricato {}", newProductEvent.getProductId());
  }
  
  @SneakyThrows
  @KafkaListener(id = "message-service2", topics = "NewOrder")
  public void handleNewOrder(@Payload GenericEvent genericEvent, @Headers Map headers) {
    NewOrderEvent newOrderEvent = objectMapper.readValue(genericEvent.getPayload(), NewOrderEvent.class);
    
    ConversationResponse conversationResponse = conversationService.newConversation(ConversationRequest.builder()
      .user1(newOrderEvent.getBuyer())
      .user2(newOrderEvent.getProduct())
      .prodottoCorrelato(newOrderEvent.getProduct())
      .build());
    
    conversationService.addMessageToConversation(conversationResponse.getId(),
      MessageRequest.builder()
        .to(newOrderEvent.getSeller())
        .from("winted")
        .messageType(MsgType.SYSTEM.toString())
        .content("Il tuo oggetto e' stato comprato, scarica l'etichetta. Link etichetta")
        .timestamp(LocalDateTime.now().toString())
        .build());
    
    conversationService.addMessageToConversation(conversationResponse.getId(),
      MessageRequest.builder()
        .to(newOrderEvent.getBuyer())
        .from("winted")
        .messageType(MsgType.SYSTEM.toString())
        .content("Attendi che il venditore invii il pacco")
        .timestamp(LocalDateTime.now().toString())
        .build());
    
  }

//  @KafkaListener(id = "message-service3", topics = "NewReplay")
//  public void handleNewReplay(NewReplayEvent newReplayEvent) {
//    log.info("L'utente {} ha risposto all'utente {}", newReplayEvent.getActor1Id(), newReplayEvent.getActor2Id());
//  }
//
//  @KafkaListener(id = "message-service4", topics = "NewOffer")
//  public void handleNewOffer(NewOfferEvent newOfferEvent) {
//    log.info("L'utente {} ha fatto un'offerta all'utente {} per il prodotto {} al prezzo di {}",
//      newOfferEvent.getSellerId(), newOfferEvent.getBuyerId(), newOfferEvent.getProductId(),
//      newOfferEvent.getPrice());
//  }
//
//  @KafkaListener(id = "message-service", topics = "OfferAccepted")
//  public void handleOfferAccepted(OfferAcceptedEvent offerAcceptedEvent) {
//    if (offerAcceptedEvent.getIsAccepted()) {
//      log.info("L'utente {} ha accettato l'offerta dell'utente {} per il prodotto {} al prezzo di {}",
//        offerAcceptedEvent.getBuyerId(), offerAcceptedEvent.getSellerId(), offerAcceptedEvent.getProductId(),
//        offerAcceptedEvent.getPrice());
//    } else {
//      log.info("L'utente {} non ha accettato l'offerta dell'utente {} per il prodotto {} al prezzo di {}",
//        offerAcceptedEvent.getBuyerId(), offerAcceptedEvent.getSellerId(), offerAcceptedEvent.getProductId(),
//        offerAcceptedEvent.getPrice());
//    }
//  }
}
