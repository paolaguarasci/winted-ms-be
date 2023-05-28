package it.pingflood.winted.messageservice.service;

import it.pingflood.winted.messageservice.data.dto.AnteprimaInbox;
import it.pingflood.winted.messageservice.data.dto.ConversationRequest;
import it.pingflood.winted.messageservice.data.dto.ConversationResponse;
import it.pingflood.winted.messageservice.data.dto.MessageRequest;

import java.util.List;

public interface ConversationService {
  List<AnteprimaInbox> getAllConversationPreviewFromLoggedUser();
  
  ConversationResponse getOneById(String id);
  
  ConversationResponse addMessageToConversation(String id, MessageRequest messageRequest);
  
  ConversationResponse newConversation(ConversationRequest conversationRequest);
}
