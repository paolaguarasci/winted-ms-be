package it.pingflood.winted.messageservice.controller;

import it.pingflood.winted.messageservice.data.dto.AnteprimaInbox;
import it.pingflood.winted.messageservice.data.dto.MessageListResponse;
import it.pingflood.winted.messageservice.data.dto.MessageResponse;
import it.pingflood.winted.messageservice.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api/v1/message")
public class MessageController {
  
  private final MessageService messageService;
  
  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }
  
  // sarebbe inbox preview sul frontend
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<List<AnteprimaInbox>> getAllConversationFromLoggedUser() {
    return ResponseEntity.of(Optional.of(messageService.getAllConversationPreviewFromLoggedUser()));
  }
  
  @GetMapping("/from/{userid}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<Map<String, List<MessageResponse>>> getAllConversationFromUsername(@PathVariable(value = "userid") String userid) {
    return ResponseEntity.of(Optional.of(messageService.getAllConversationFromUser(userid)));
  }
  
  @GetMapping("/to/{userid}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<Map<String, List<MessageResponse>>> getAllConversationToUsername(@PathVariable(value = "userid") String userid) {
    return ResponseEntity.of(Optional.of(messageService.getAllConversationToUser(userid)));
  }
  
  @GetMapping("/from/{userid1}/to/{userid2}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<MessageListResponse> getConversation(@PathVariable(value = "userid1") String userid1, @PathVariable(value = "userid2") String userid2) {
    return ResponseEntity.of(Optional.of(messageService.getConversation(userid1, userid2)));
  }
//
//  @PostMapping()
//  @ResponseStatus(HttpStatus.CREATED)
//  public ResponseEntity<MessageResponse> saveMessage(@PathVariable(value = "username1") String username1, @PathVariable(value = "username1") String username2, @RequestBody MessageRequest messageRequest) {
//    return ResponseEntity.of(Optional.of(messageService.saveMessage(username1, username2, messageRequest)));
//  }
}
