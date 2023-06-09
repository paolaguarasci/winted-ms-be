package it.pingflood.winted.messageservice.service;

import it.pingflood.winted.messageservice.data.dto.NotificaPOSTRequest;
import it.pingflood.winted.messageservice.data.dto.NotificaRequest;
import it.pingflood.winted.messageservice.data.dto.NotificaResponse;

import java.util.List;

public interface NotificaService {
  List<NotificaResponse> getAllByLoggedUser(String name);
  
  List<NotificaResponse> marksAllRead(List<NotificaRequest> notificaRequests, String name);
  
  NotificaResponse createNotifica(NotificaPOSTRequest notificaRequest);
  
  NotificaResponse markOneRead(String id, NotificaRequest notificaRequests, String name);
}
