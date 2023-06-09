package it.pingflood.winted.messageservice.service.impl;

import it.pingflood.winted.messageservice.data.Conversation;
import it.pingflood.winted.messageservice.data.Message;
import it.pingflood.winted.messageservice.data.dto.*;
import it.pingflood.winted.messageservice.repository.ConversationRepository;
import it.pingflood.winted.messageservice.repository.MessageRepository;
import it.pingflood.winted.messageservice.service.ConversationService;
import it.pingflood.winted.messageservice.service.NotificaService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@Transactional
public class ConversationServiceImpl implements ConversationService {
  private final ConversationRepository conversationRepository;
  private final ModelMapper modelMapper;
  private final PrettyTime prettyTime;
  private final MessageRepository messageRepository;
  private final NotificaService notificaService;
  
  public ConversationServiceImpl(ConversationRepository conversationRepository, MessageRepository messageRepository, NotificaService notificaService) {
    this.messageRepository = messageRepository;
    this.notificaService = notificaService;
    this.prettyTime = new PrettyTime();
    this.modelMapper = new ModelMapper();
    this.modelMapper.getConfiguration()
      .setFieldMatchingEnabled(true)
      .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
    this.conversationRepository = conversationRepository;
  }
  
  
  @Override
  public List<AnteprimaInbox> getAllConversationPreviewFromLoggedUser(String loggedUserid) {
    List<Conversation> conversazioni = conversationRepository.findAllByUser1IsOrUser2Is(loggedUserid, loggedUserid);
    return conversazioni.stream().map(conversation -> {
        String lastMessagePreview = "";
        String timeAgo = "";
        if (conversation.getMessages() != null && !conversation.getMessages().isEmpty()) {
          Message lastMessage = conversation.getMessages().get(conversation.getMessages().size() - 1);
          lastMessagePreview = lastMessage.getContent();
          timeAgo = prettyTime.format(lastMessage.getTimestamp());
        }
        
        String altroUtente = conversation.getUser1().equals(loggedUserid) ? conversation.getUser2() : conversation.getUser1();
        
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
  public ConversationResponse getOneById(String id, String loggedUserid) {
    Conversation conv = conversationRepository.findById(id).orElseThrow();
    return filteredConversation(conv, loggedUserid);
  }
  
  private ConversationResponse filteredConversation(Conversation conversation, String loggedUser) {
    List<Message> messagesOriginal = conversation.getMessages();
    List<Message> messagesOriginal1 = messagesOriginal.stream()
      .filter(message -> message.getTo().equals(loggedUser) || message.getFrom().equals(loggedUser))
      .toList();
    messagesOriginal1.forEach(message -> message.setTimeAgo(prettyTime.format(message.getTimestamp())));
    String altroUtente = conversation.getUser1().equals(loggedUser) ? conversation.getUser2() : conversation.getUser1();
    
    ConversationResponse convResp = modelMapper.map(conversation, ConversationResponse.class);
    convResp.setMessages(messagesOriginal1.stream().map(message -> modelMapper.map(message, MessageResponse.class)).toList());
    convResp.setLoggedUser(loggedUser);
    convResp.setAltroUtente(altroUtente);
    return convResp;
  }
  
  @Override
  public ConversationResponse addMessageToConversation(String id, MessageRequest messageRequest, String loggedUserid) {
    Conversation conv = conversationRepository.findById(id).orElseThrow();
    conv.getMessages().add(messageRepository.save(modelMapper.map(messageRequest, Message.class)));
    notificaService.createNotifica(NotificaPOSTRequest.builder()
      .content("Nuovo messaggio")
      .user(messageRequest.getTo())
      .prodottoCorrelato(conv.getProdottoCorrelato())
      .build());
    return filteredConversation(conversationRepository.save(conv), loggedUserid);
  }
  
  
  @Override
  public ConversationResponse newConversation(ConversationRequest conversationRequest, String principal) {
    log.info("Nuova conversazione tra {} e {} - logged user {}", conversationRequest.getUser1(), conversationRequest.getUser2(), principal);

//    if ((!Objects.equals(conversationRequest.getUser1(), principal) && !Objects.equals(conversationRequest.getUser2(), principal))) {
//      throw new IllegalArgumentException("non puoi iniziare una conversazione senza essere loggato");
//    }
    
    List<Conversation> older = findGenericByUsers(conversationRequest.getUser1(), conversationRequest.getUser2());
    
    if (!older.isEmpty() && (conversationRequest.getProdottoCorrelato() == null || conversationRequest.getProdottoCorrelato().equals(""))) {
      return modelMapper.map(older.get(0), ConversationResponse.class);
    }
    
    
    conversationRequest.setMessages(new ArrayList<>());
    return modelMapper.map(conversationRepository.save(modelMapper.map(conversationRequest, Conversation.class)), ConversationResponse.class);
  }
  
  private List<Conversation> findGenericByUsers(String username1, String username2) {
    List<Conversation> conversazioni1 = conversationRepository.findAllByUser1IsOrUser2Is(username1, username2);
    List<Conversation> conversazioni2 = conversationRepository.findAllByUser1IsOrUser2Is(username2, username1);
    List<Conversation> conversations = Stream.concat(conversazioni1.stream(), conversazioni2.stream()).toList();
    return conversations.stream()
      .filter(conversation -> conversation.getProdottoCorrelato() == null || conversation.getProdottoCorrelato().equals("")).collect(Collectors.toList());
  }
  
}
