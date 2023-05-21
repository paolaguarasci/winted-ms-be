package it.pingflood.winted.messageservice.service.impl;

import it.pingflood.winted.messageservice.data.Conversation;
import it.pingflood.winted.messageservice.data.Message;
import it.pingflood.winted.messageservice.data.dto.AnteprimaInbox;
import it.pingflood.winted.messageservice.data.dto.MessageListResponse;
import it.pingflood.winted.messageservice.data.dto.MessageRequest;
import it.pingflood.winted.messageservice.data.dto.MessageResponse;
import it.pingflood.winted.messageservice.repository.ConversationRepository;
import it.pingflood.winted.messageservice.repository.MessageRepository;
import it.pingflood.winted.messageservice.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@Slf4j
@Transactional
public class MessageServiceImpl implements MessageService {
  private final ConversationRepository conversationRepository;
  private final MessageRepository messageRepository;
  private final ModelMapper modelMapper;
  private final PrettyTime prettyTime;
  
  public MessageServiceImpl(MessageRepository messageRepository,
                            ConversationRepository conversationRepository) {
    this.messageRepository = messageRepository;
    modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
      .setFieldMatchingEnabled(true)
      .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
    this.conversationRepository = conversationRepository;
    prettyTime = new PrettyTime();
  }
  
  @Override
  public MessageListResponse getConversation(String u1, String u2) {
    List<Message> conversation = messageRepository.findAllByFromIsInAndToIsInOrderByTimestampDesc(List.of(u1, u2), List.of(u1, u2));
    return MessageListResponse.builder().messaggi(conversation.stream().map(message -> modelMapper.map(message, MessageResponse.class)).collect(Collectors.toList())).build();
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
  
  @Override
  public List<AnteprimaInbox> getAllConversationPreviewFromLoggedUser() {
    
    String loggedUsername = "paola";
    
    List<Conversation> conversazioni = conversationRepository.findAllByUser1IsOrUser2Is(loggedUsername, loggedUsername);
    
    return conversazioni.stream().map(conversation -> {
        String lastMessagePreview = "";
        String timeAgo = "";
        if (conversation.getMessages().size() > 0) {
          Message lastMessage = conversation.getMessages().get(conversation.getMessages().size() - 1);
          lastMessagePreview = lastMessage.getContent();
          timeAgo = prettyTime.format(lastMessage.getTimestamp());
        }
        
        String altroUtente = conversation.getUser1().equals(loggedUsername) ? conversation.getUser2() : conversation.getUser1();
        
        return AnteprimaInbox.builder()
          .conversationId(conversation.getId())
          .lastMessage(lastMessagePreview)
          .timeAgo(timeAgo)
          .prodottoCorrelato(conversation.getProdottoCorrelato())
          .altroUtente(altroUtente)
          .build();
      }
    ).collect(Collectors.toList());
  }
}
