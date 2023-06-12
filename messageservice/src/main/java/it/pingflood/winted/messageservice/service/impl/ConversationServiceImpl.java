package it.pingflood.winted.messageservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pingflood.winted.messageservice.client.OrderClient;
import it.pingflood.winted.messageservice.client.dto.NewCheckOutRequest;
import it.pingflood.winted.messageservice.client.dto.NewCheckOutResponse;
import it.pingflood.winted.messageservice.data.Conversation;
import it.pingflood.winted.messageservice.data.Message;
import it.pingflood.winted.messageservice.data.MsgType;
import it.pingflood.winted.messageservice.data.dto.*;
import it.pingflood.winted.messageservice.repository.ConversationRepository;
import it.pingflood.winted.messageservice.repository.MessageRepository;
import it.pingflood.winted.messageservice.service.ConversationService;
import it.pingflood.winted.messageservice.service.NotificaService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
  private final String wintedId;
  private final ObjectMapper objectMapper;
  private final OrderClient orderClient;
  private final String tokenPrefix;
  private final SimpMessagingTemplate simpMessagingTemplate;
  
  public ConversationServiceImpl(ConversationRepository conversationRepository, MessageRepository messageRepository, NotificaService notificaService, ObjectMapper objectMapper, OrderClient orderClient, SimpMessagingTemplate simpMessagingTemplate) {
    this.objectMapper = objectMapper;
    this.orderClient = orderClient;
    this.simpMessagingTemplate = simpMessagingTemplate;
    this.tokenPrefix = "Bearer ";
    wintedId = "6484fb28cb521302e093d93c";
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
      ConversationResponse convFiltered = filteredConversation(conversation, loggedUserid);
      log.info("Conv filtered {}", convFiltered);
      if (convFiltered.getMessages() != null && !convFiltered.getMessages().isEmpty()) {
        MessageResponse lastMessage = convFiltered.getMessages().get(convFiltered.getMessages().size() - 1);
        log.info("Conv filtered last message {}", lastMessage);
        log.info("Conv filtered last message timestamp {}", lastMessage.getTimestamp());
        lastMessagePreview = lastMessage.getContent();
        timeAgo = lastMessage.getTimeAgo();
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
      .filter(message -> {
        if ((message.getTo().equals(loggedUser) && message.getMessageType() == MsgType.SYSTEM_REQUEST)) {
          return message.getNeedAnswer();
        }
        return (message.getTo().equals(loggedUser) || message.getFrom().equals(loggedUser));
      }).toList();
    
    messagesOriginal1.forEach(message -> message.setTimeAgo(prettyTime.format(message.getTimestamp())));
    String altroUtente = conversation.getUser1().equals(loggedUser) ? conversation.getUser2() : conversation.getUser1();
    
    ConversationResponse convResp = modelMapper.map(conversation, ConversationResponse.class);
    convResp.setMessages(messagesOriginal1.stream().map(message -> modelMapper.map(message, MessageResponse.class)).toList());
    convResp.setLoggedUser(loggedUser);
    convResp.setAltroUtente(altroUtente);
    return convResp;
  }
  
  @Override
  public ConversationResponse addMessageToConversation(String id, MessageRequest messageRequest, String loggedUserid, String token) {
    log.info("Ricevuto nuovo messaggio {}", messageRequest);
    Conversation conv = conversationRepository.findById(id).orElseThrow();
    messageRequest.setTimestamp(LocalDateTime.now().toString());
    String notificaMsg = null;
    if (messageRequest.getMessageType().equals(MsgType.TESTO.toString()) || messageRequest.getMessageType().equals(MsgType.SYSTEM.toString())) {
      conv.getMessages().add(messageRepository.save(Message.builder()
        .messageType(MsgType.valueOf(messageRequest.getMessageType()))
        .from(messageRequest.getFrom())
        .to(messageRequest.getTo())
        .content(messageRequest.getContent())
        .timestamp(LocalDateTime.now())
        .offerta(null)
        .needAnswer(false)
        .isAnswerTo(null)
        .build()));
      notificaMsg = "Nuovo messaggio";
    }
    
    if (messageRequest.getMessageType().equals(MsgType.OFFERT_REQUEST.toString())) {
      log.info("Ricevuto nuovo messaggio OFFERTA {}", messageRequest);
      notificaMsg = "Nuova offerta!";
      
      conv.getMessages().add(messageRepository.save(Message.builder()
        .messageType(MsgType.SYSTEM)
        .from(wintedId)
        .to(messageRequest.getFrom())
        .content("Offerta inviata")
        .timestamp(LocalDateTime.now())
        .offerta(BigDecimal.valueOf(Double.parseDouble(messageRequest.getOfferta())))
        .needAnswer(false)
        .isAnswerTo(null)
        .build()));
      
      conv.getMessages().add(messageRepository.save(Message.builder()
        .messageType(MsgType.SYSTEM_REQUEST)
        .from(wintedId)
        .to(messageRequest.getTo())
        .content(messageRequest.getContent())
        .timestamp(LocalDateTime.now())
        .offerta(BigDecimal.valueOf(Double.parseDouble(messageRequest.getOfferta())))
        .needAnswer(true)
        .isAnswerTo(null)
        .build()));
    }
    
    if (messageRequest.getMessageType().equals(MsgType.OFFERT_RESPONSE.toString()) && messageRequest.getContent().contains("yes")) {
      notificaMsg = "Offerta accettata";
      log.info("Ricevuto nuovo messaggio OFFERTA OK {}", messageRequest);
      Message requestMessage = messageRepository.findById(messageRequest.getIsAnswerTo()).orElseThrow();
      NewCheckOutResponse checkout = getCheckout(conv.getProdottoCorrelato(), conv.getUser1(), requestMessage.getOfferta(), loggedUserid, token);
      if (checkout == null) {
        conv.getMessages().add(messageRepository.save(Message.builder()
          .messageType(MsgType.SYSTEM)
          .from(wintedId)
          .to(loggedUserid)
          .content("Errore - offerta non accettabile")
          .timestamp(LocalDateTime.now())
          .isAnswerTo(messageRequest.getIsAnswerTo())
          .build()));
        conv.getMessages().add(messageRepository.save(Message.builder()
          .messageType(MsgType.SYSTEM)
          .from(wintedId)
          .to(messageRequest.getTo())
          .content("Errore - offerta non accettabile")
          .timestamp(LocalDateTime.now())
          .isAnswerTo(messageRequest.getIsAnswerTo())
          .build()));
      } else {
        requestMessage.setNeedAnswer(false);
        messageRepository.save(requestMessage);
        conv.getMessages().add(messageRepository.save(Message.builder()
          .messageType(MsgType.SYSTEM)
          .from(wintedId)
          .to(messageRequest.getTo())
          .content("Offerta accettata")
          .timestamp(LocalDateTime.now())
          .isAnswerTo(messageRequest.getIsAnswerTo())
          .needAnswer(false)
          .build()));
        conv.getMessages().add(messageRepository.save(Message.builder()
          .messageType(MsgType.SYSTEM)
          .from(wintedId)
          .to(loggedUserid)
          .content("Hai accettato l'offerta, attendi che il compratore effettui il checkout")
          .timestamp(LocalDateTime.now())
          .isAnswerTo(messageRequest.getIsAnswerTo())
          .needAnswer(false)
          .build()));
      }
      Message m2 = conv.getMessages().stream().filter(message -> message.getId().equals(requestMessage.getId())).toList().get(0);
      m2.setNeedAnswer(false);
    }
    
    if (messageRequest.getMessageType().equals(MsgType.OFFERT_RESPONSE.toString()) && messageRequest.getContent().contains("no")) {
      log.info("Ricevuto nuovo messaggio OFFERTA NO {}", messageRequest);
      notificaMsg = "Offerta rifiutata";
      Message requestMessage = messageRepository.findById(messageRequest.getIsAnswerTo()).orElseThrow();
      Message m2 = conv.getMessages().stream().filter(message -> message.getId().equals(requestMessage.getId())).toList().get(0);
      m2.setNeedAnswer(false);
      log.info("Risposta a DOPO SAVE {}", requestMessage);
      conv.getMessages().add(messageRepository.save(Message.builder()
        .messageType(MsgType.SYSTEM)
        .from(wintedId)
        .to(messageRequest.getTo())
        .content("Offerta rifiutata")
        .timestamp(LocalDateTime.now())
        .isAnswerTo(messageRequest.getIsAnswerTo())
        .build()));
      
      conv.getMessages().add(messageRepository.save(Message.builder()
        .messageType(MsgType.SYSTEM)
        .from(wintedId)
        .to(loggedUserid)
        .content("Hai rifiutato l'offerta!")
        .timestamp(LocalDateTime.now())
        .isAnswerTo(messageRequest.getIsAnswerTo())
        .needAnswer(false)
        .build()));
    }
    
    notificaService.createNotifica(NotificaPOSTRequest.builder()
      .content(notificaMsg)
      .user(messageRequest.getTo())
      .prodottoCorrelato(conv.getProdottoCorrelato())
      .build());
    simpMessagingTemplate.convertAndSend("/room/" + conv.getId(), SocketDTO.builder().message("update").build());
    return filteredConversation(conversationRepository.save(conv), loggedUserid);
  }
  
  // TODO Risolvere alcuni problemi prima
  //
  // problema 1: prima il problema di una comunicazione sicura tra servizi (non accessibile dall'esterno, nemmeno da
  // loggato. Possibile soluzione liv auth ADMIN per l'endpoint ricevente, ma il token chi me lo da? mi devo loggare
  // prima? RISOLTO IN PARTE (check sul principal che crea il checkout)
  //
  // problema 2: entity offerta? Perche' altrimenti al momento del checkout/ordine (nel service ordine)
  // il prezzo e' sempre preso dalla base di dati ed e' quello del prodotto stesso.
  // Se devo cambiare il prezzo del prodotto mi serve un item che leghi insieme prodotto, buyer e nuovoPrezzo,
  // costruito server side ovviamente.
  //
  // E quindi... risolti questi due problemi in questa chiamata triggero la generazione di una nuova offerta, non di
  // nuovo checkout, ed al momento della richiesta normale di nuovo checkout (come sempre avviene per i nuovi acquisti)
  // controllo che ci sia una nuova offerta (per quell'utente, su quel prodotto, a quel prezzo)
  //
  // ... quindi l'ordine ha un prezzo che cambia in base al fatto che ci sia o meno un offerta.
  // forse RISOLTO...
  
  @SneakyThrows
  private NewCheckOutResponse getCheckout(String productId, String buyer, BigDecimal newPrice, String principal, String token) {
    
    NewCheckOutRequest newCheckOutRequest = NewCheckOutRequest.builder()
      .price(newPrice.toString())
      .product(productId)
      .buyer(buyer)
      .build();
    String response = null;
    try {
      response = orderClient.postNewCheckout(this.tokenPrefix + token, newCheckOutRequest);
      log.info("RISPOSTA da ordine {}", response);
      NewCheckOutResponse newCheckOutResponse = objectMapper.readValue(response, NewCheckOutResponse.class);
      log.info("RISPOSTA da ordine obj {}", newCheckOutResponse);
      return newCheckOutResponse;
    } catch (Exception e) {
      return null;
    }
    
    
  }
  
  @Override
  public ConversationResponse newConversation(ConversationRequest conversationRequest, String principal) {
    
    List<Conversation> older = null;
    
    if (conversationRequest.getProdottoCorrelato() == null || conversationRequest.getProdottoCorrelato().equals("")) {
      older = findGenericByUsers(conversationRequest.getUser1(), conversationRequest.getUser2());
    } else {
      older = findGenericByUsersAndProduct(conversationRequest.getUser1(), conversationRequest.getUser2(), conversationRequest.getProdottoCorrelato());
    }
    
    if (older != null && !older.isEmpty()) {
      return modelMapper.map(older.get(0), ConversationResponse.class);
    }
    
    conversationRequest.setMessages(new ArrayList<>());
    // conversationRequest.setProdottoCorrelato(conversationRequest.getProdottoCorrelato());
    return filteredConversation(conversationRepository.save(modelMapper.map(conversationRequest, Conversation.class)), principal);
  }
  
  private List<Conversation> findGenericByUsersAndProduct(String username1, String username2, String prodottoCorrelato) {
    List<Conversation> conversazioni1 = conversationRepository.findAllByUser1IsAndUser2IsAndProdottoCorrelatoIs(username1, username2, prodottoCorrelato);
    List<Conversation> conversazioni2 = conversationRepository.findAllByUser1IsAndUser2IsAndProdottoCorrelatoIs(username2, username1, prodottoCorrelato);
    return Stream.concat(conversazioni1.stream(), conversazioni2.stream()).toList();
  }
  
  private List<Conversation> findGenericByUsers(String username1, String username2) {
    List<Conversation> conversazioni1 = conversationRepository.findAllByUser1IsAndUser2Is(username1, username2);
    List<Conversation> conversazioni2 = conversationRepository.findAllByUser1IsAndUser2Is(username2, username1);
    List<Conversation> conversations = Stream.concat(conversazioni1.stream(), conversazioni2.stream()).toList();
    return conversations.stream()
      .filter(conversation -> conversation.getProdottoCorrelato() == null || conversation.getProdottoCorrelato().equals("")).collect(Collectors.toList());
  }
  
}
