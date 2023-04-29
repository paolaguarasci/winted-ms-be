package it.pingflood.winted.messageservice.controller;

import it.pingflood.winted.messageservice.service.MessageListService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageListController {
  
  private final MessageListService messageListService;
  
  
  public MessageListController(MessageListService messageListService) {
    this.messageListService = messageListService;
  }
}
