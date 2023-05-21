package it.pingflood.winted.messageservice.service.impl;

import it.pingflood.winted.messageservice.data.Conversation;
import it.pingflood.winted.messageservice.data.Message;
import it.pingflood.winted.messageservice.data.dto.AnteprimaInbox;
import it.pingflood.winted.messageservice.data.dto.ConversationRequest;
import it.pingflood.winted.messageservice.data.dto.ConversationResponse;
import it.pingflood.winted.messageservice.data.dto.MessageRequest;
import it.pingflood.winted.messageservice.repository.ConversationRepository;
import it.pingflood.winted.messageservice.repository.MessageRepository;
import it.pingflood.winted.messageservice.service.ConversationService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional

public class ConversationServiceImpl implements ConversationService {
  private final ConversationRepository conversationRepository;
  private final ModelMapper modelMapper;
  private final PrettyTime prettyTime;
  private final MessageRepository messageRepository;
  
  public ConversationServiceImpl(ConversationRepository conversationRepository, MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
    this.prettyTime = new PrettyTime();
    this.modelMapper = new ModelMapper();
    this.modelMapper.getConfiguration()
      .setFieldMatchingEnabled(true)
      .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
    this.conversationRepository = conversationRepository;
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
  
  @Override
  public ConversationResponse getOneById(String id) {
    String loggedUsername = "paola";
    Conversation conv = conversationRepository.findById(id).orElseThrow();
    conv.getMessages().forEach(message -> message.setTimeAgo(prettyTime.format(message.getTimestamp())));
    String altroUtente = conv.getUser1().equals(loggedUsername) ? conv.getUser2() : conv.getUser1();
    ConversationResponse convResp = modelMapper.map(conv, ConversationResponse.class);
    convResp.setLoggedUser(loggedUsername);
    convResp.setAltroUtente(altroUtente);
    return convResp;
  }
  
  @Override
  public ConversationResponse addMessageToConversation(String id, MessageRequest messageRequest) {
    Conversation conv = conversationRepository.findById(id).orElseThrow();
    conv.getMessages().add(messageRepository.save(modelMapper.map(messageRequest, Message.class)));
    return modelMapper.map(conversationRepository.save(conv), ConversationResponse.class);
  }
  
  @Override
  public ConversationResponse newConversation(ConversationRequest conversationRequest) {
    return modelMapper.map(conversationRepository.save(modelMapper.map(conversationRequest, Conversation.class)), ConversationResponse.class);
  }
}
