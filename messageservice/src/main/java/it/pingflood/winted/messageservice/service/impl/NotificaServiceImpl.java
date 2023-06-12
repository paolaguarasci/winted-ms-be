package it.pingflood.winted.messageservice.service.impl;

import it.pingflood.winted.messageservice.data.Notifica;
import it.pingflood.winted.messageservice.data.dto.NotificaPOSTRequest;
import it.pingflood.winted.messageservice.data.dto.NotificaRequest;
import it.pingflood.winted.messageservice.data.dto.NotificaResponse;
import it.pingflood.winted.messageservice.data.dto.SocketDTO;
import it.pingflood.winted.messageservice.repository.NotificaRepository;
import it.pingflood.winted.messageservice.service.NotificaService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class NotificaServiceImpl implements NotificaService {
  private final NotificaRepository notificaRepository;
  private final ModelMapper modelMapper;
  
  private final SimpMessagingTemplate simpMessagingTemplate;
  
  public NotificaServiceImpl(NotificaRepository notificaRepository, SimpMessagingTemplate simpMessagingTemplate) {
    this.notificaRepository = notificaRepository;
    this.simpMessagingTemplate = simpMessagingTemplate;
    this.modelMapper = new ModelMapper();
  }
  
  @Override
  public List<NotificaResponse> getAllByLoggedUser(String name) {
    return notificaRepository.findAllByUser(name).stream().map((element) -> modelMapper.map(element, NotificaResponse.class)).collect(Collectors.toList());
  }
  
  @Override
  public List<NotificaResponse> marksAllRead(List<NotificaRequest> notificaRequests, String name) {
    List<String> ids = notificaRequests.stream().map(NotificaRequest::getId).toList();
    List<Notifica> notifiche = notificaRepository.findAllById(ids);
    notifiche.forEach(notifica -> notifica.setRead(true));
    return notificaRepository.saveAll(notifiche).stream().map(notifica -> modelMapper.map(notifica, NotificaResponse.class)).toList();
  }
  
  @Override
  public NotificaResponse createNotifica(NotificaPOSTRequest notificaRequest) {
    Notifica notifica = Notifica.builder()
      .user(notificaRequest.getUser())
      .prodottoCorrelato(notificaRequest.getProdottoCorrelato())
      .content(notificaRequest.getContent())
      .timestamp(LocalDateTime.now())
      .read(false)
      .timeAgo("")
      .build();
    log.info("Nuova notifica per {}", notificaRequest.getUser());

//    SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
//    headerAccessor.setSessionId("guest");
//    headerAccessor.setLeaveMutable(true);
//    simpMessagingTemplate.convertAndSendToUser("guest", "/notify", SocketDTO.builder().message("update").build(), headerAccessor.getMessageHeaders());
    String username = notificaRequest.getUser();
    String queueName = "/notify/" + username;
    simpMessagingTemplate.convertAndSend(queueName, SocketDTO.builder().message("update").build());
    
    return modelMapper.map(notificaRepository.save(notifica), NotificaResponse.class);
  }
  
  @Override
  public NotificaResponse markOneRead(String id, NotificaRequest notificaRequests, String name) {
    Notifica notifica = notificaRepository.findById(id).orElseThrow();
    if (!notifica.getUser().equals(name)) {
      throw new IllegalArgumentException("Wrog!");
    }
    notifica.setRead(true);
//    simpMessagingTemplate.convertAndSendToUser(null, "/notify", SocketDTO.builder().message("update").build());
    return modelMapper.map(notificaRepository.save(notifica), NotificaResponse.class);
  }
}
