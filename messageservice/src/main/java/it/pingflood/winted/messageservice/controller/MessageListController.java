package it.pingflood.winted.messageservice.controller;

import it.pingflood.winted.messageservice.data.dto.MessageListDTO;
import it.pingflood.winted.messageservice.data.dto.ParticipantDTO;
import it.pingflood.winted.messageservice.service.MessageListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api/v1/message")
public class MessageListController {
  
  private final MessageListService messageListService;
  
  public MessageListController(MessageListService messageListService) {
    this.messageListService = messageListService;
  }
  
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<List<MessageListDTO>> getMessageLists() {
    return ResponseEntity.of(Optional.of(messageListService.getALl()));
  }
  
  @GetMapping("/{username}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<MessageListDTO> getMessageListByUsername(@PathVariable(value = "username") ParticipantDTO participant) {
    log.debug("richiesta message utente {}", participant);
    return ResponseEntity.of(Optional.of(messageListService.getMessagesListByParticipant(participant)));
  }
}
