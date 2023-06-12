package it.pingflood.winted.messageservice.controller;

import it.pingflood.winted.messageservice.data.dto.*;
import it.pingflood.winted.messageservice.service.ConversationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/conversation")
public class ConversationController {
  private final ConversationService conversationService;
  private final SimpMessagingTemplate simpMessagingTemplate;
  
  public ConversationController(ConversationService conversationService, SimpMessagingTemplate simpMessagingTemplate) {
    this.conversationService = conversationService;
    this.simpMessagingTemplate = simpMessagingTemplate;
  }
  
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<List<AnteprimaInbox>> getAllConversationFromLoggedUser(Principal principal) {
    return ResponseEntity.ok(conversationService.getAllConversationPreviewFromLoggedUser(principal.getName()));
  }
  
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ConversationResponse> addMessageAndInitConversation(@RequestBody ConversationRequest conversationRequest, Principal proncipal) {
    return ResponseEntity.ok(conversationService.newConversation(conversationRequest, proncipal.getName()));
  }
  
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ConversationResponse> getOneById(@PathVariable String id, Principal principal) {
    return ResponseEntity.ok(conversationService.getOneById(id, principal.getName()));
  }
  
  @PostMapping("/{id}/newmsg")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ConversationResponse> addMessageToConversation(@PathVariable String id, @RequestBody MessageRequest messageRequest, Principal proncipal, @AuthenticationPrincipal Jwt token) {
    simpMessagingTemplate.convertAndSend("/room/" + id, SocketDTO.builder().message("update").build());
    return ResponseEntity.ok(conversationService.addMessageToConversation(id, messageRequest, proncipal.getName(), token.getTokenValue()));
  }
}
