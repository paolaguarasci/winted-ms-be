package it.pingflood.winted.messageservice.service;

import it.pingflood.winted.messageservice.data.dto.NotificaRequest;
import it.pingflood.winted.messageservice.data.dto.NotificaResponse;

import java.util.List;

public interface NotificaService {
  List<NotificaResponse> getAllByLoggedUser();
  
  List<NotificaResponse> marksAllRead(List<NotificaRequest> notificaRequests);
}
