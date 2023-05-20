package it.pingflood.winted.messageservice.service;

import it.pingflood.winted.messageservice.data.dto.MessageListResponse;
import it.pingflood.winted.messageservice.data.dto.MessageRequest;
import it.pingflood.winted.messageservice.data.dto.MessageResponse;

import java.util.List;
import java.util.Map;

public interface MessageService {
  MessageListResponse getConversation(String u1, String u2);
  
  MessageResponse saveMessage(String u1, String u2, MessageRequest messageRequest);
  
  Map<String, List<MessageResponse>> getAllConversationFromUsername(String username);
  
  Map<String, List<MessageResponse>> getAllConversationToUsername(String username);
}
