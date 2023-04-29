package it.pingflood.winted.messageservice.service;

import it.pingflood.winted.messageservice.data.dto.MessageListDTO;
import it.pingflood.winted.messageservice.data.dto.ParticipantDTO;

import java.util.List;


public interface MessageListService {
  List<MessageListDTO> getALl();
  
  MessageListDTO getMessagesListByParticipant(ParticipantDTO participant);
}
