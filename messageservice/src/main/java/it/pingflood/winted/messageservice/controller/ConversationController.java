package it.pingflood.winted.messageservice.controller;

import it.pingflood.winted.messageservice.data.dto.AnteprimaInbox;
import it.pingflood.winted.messageservice.data.dto.ConversationResponse;
import it.pingflood.winted.messageservice.data.dto.MessageRequest;
import it.pingflood.winted.messageservice.service.ConversationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/conversation")
public class ConversationController {
  private final ConversationService conversationService;
  
  public ConversationController(ConversationService conversationService) {
    this.conversationService = conversationService;
  }
  
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<List<AnteprimaInbox>> getAllConversationFromLoggedUser(Principal principal) {
    return ResponseEntity.ok(conversationService.getAllConversationPreviewFromLoggedUser(principal.getName()));
  }
  
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ConversationResponse> getOneById(@PathVariable String id, Principal principal) {
    return ResponseEntity.ok(conversationService.getOneById(id, principal.getName()));
  }
  
  @PostMapping("/{id}/newmsg")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ConversationResponse> addMessageToConversation(@PathVariable String id, @RequestBody MessageRequest messageRequest, Principal proncipal) {
    return ResponseEntity.ok(conversationService.addMessageToConversation(id, messageRequest, proncipal.getName()));
  }
}
