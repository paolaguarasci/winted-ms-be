package it.pingflood.winted.messageservice.service.impl;

import it.pingflood.winted.messageservice.data.Message;
import it.pingflood.winted.messageservice.data.dto.MessageListResponse;
import it.pingflood.winted.messageservice.data.dto.MessageRequest;
import it.pingflood.winted.messageservice.data.dto.MessageResponse;
import it.pingflood.winted.messageservice.repository.MessageRepository;
import it.pingflood.winted.messageservice.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;

@Service
@Slf4j
@Transactional
public class MessageServiceImpl implements MessageService {
  private final MessageRepository messageRepository;
  private final ModelMapper modelMapper;
  
  public MessageServiceImpl(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
    modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
      .setFieldMatchingEnabled(true)
      .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
  }
  
  @Override
  public MessageListResponse getConversation(String u1, String u2) {
    List<Message> conversation = messageRepository.findAllByFromIsInAndToIsInOrderByTimestampDesc(List.of(u1, u2), List.of(u1, u2));
    return MessageListResponse.builder().messageList(conversation.stream().map(message -> modelMapper.map(message, MessageResponse.class)).toList()).build();
  }
  
  @Override
  public MessageResponse saveMessage(String u1, String u2, MessageRequest messageResponse) {
    Optional<Message> oldMsg = messageRepository.findByFromEqualsIgnoreCaseAndToEqualsIgnoreCaseAndTimestampEquals(u1, u2, LocalDateTime.parse(messageResponse.getTimestamp()));
    if (oldMsg.isPresent()) {
      throw new IllegalArgumentException();
    }
    return modelMapper.map(messageRepository.save(modelMapper.map(messageResponse, Message.class)), MessageResponse.class);
  }
  
  @Override
  public Map<String, List<MessageResponse>> getAllConversationFromUsername(String username) {
    List<Message> res = messageRepository.findAllByFromIsIgnoreCase(username);
    Map<String, List<Message>> listByTo = res.stream().collect(groupingBy(Message::getTo));
    
    Map<String, List<MessageResponse>> listToResponse = new HashMap<>();
    
    listByTo.keySet().forEach(key -> {
      List<MessageResponse> mrlist = listByTo.get(key).stream().map(message -> modelMapper.map(message, MessageResponse.class)).toList();
      listToResponse.put(key, mrlist);
    });
    
    return listToResponse;
  }
  
  @Override
  public Map<String, List<MessageResponse>> getAllConversationToUsername(String username) {
    List<Message> res = messageRepository.findAllByToIsIgnoreCase(username);
    Map<String, List<Message>> listByTo = res.stream().collect(groupingBy(Message::getFrom));
    
    Map<String, List<MessageResponse>> listToResponse = new HashMap<>();
    
    listByTo.keySet().forEach(key -> {
      List<MessageResponse> mrlist = listByTo.get(key).stream().map(message -> modelMapper.map(message, MessageResponse.class)).toList();
      listToResponse.put(key, mrlist);
    });
    
    return listToResponse;
  }
}