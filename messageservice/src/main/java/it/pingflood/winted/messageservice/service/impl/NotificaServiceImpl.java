package it.pingflood.winted.messageservice.service.impl;

import it.pingflood.winted.messageservice.data.Notifica;
import it.pingflood.winted.messageservice.data.dto.NotificaRequest;
import it.pingflood.winted.messageservice.data.dto.NotificaResponse;
import it.pingflood.winted.messageservice.repository.NotificaRepository;
import it.pingflood.winted.messageservice.service.NotificaService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class NotificaServiceImpl implements NotificaService {
  private final NotificaRepository notificaRepository;
  private final ModelMapper modelMapper;
  
  public NotificaServiceImpl(NotificaRepository notificaRepository) {
    this.notificaRepository = notificaRepository;
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
}
