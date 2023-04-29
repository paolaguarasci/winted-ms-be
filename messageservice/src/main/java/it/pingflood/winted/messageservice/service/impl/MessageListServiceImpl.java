package it.pingflood.winted.messageservice.service.impl;

import it.pingflood.winted.messageservice.repository.MessageListRepository;
import it.pingflood.winted.messageservice.service.MessageListService;
import org.springframework.stereotype.Service;

@Service
public class MessageListServiceImpl implements MessageListService {
  private final MessageListRepository messageListRepository;
  
  public MessageListServiceImpl(MessageListRepository messageListRepository) {
    this.messageListRepository = messageListRepository;
  }
}
