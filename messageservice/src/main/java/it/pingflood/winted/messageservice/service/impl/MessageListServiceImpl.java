package it.pingflood.winted.messageservice.service.impl;

import it.pingflood.winted.messageservice.data.MessageList;
import it.pingflood.winted.messageservice.data.Participant;
import it.pingflood.winted.messageservice.data.dto.MessageListDTO;
import it.pingflood.winted.messageservice.data.dto.ParticipantDTO;
import it.pingflood.winted.messageservice.repository.MessageListRepository;
import it.pingflood.winted.messageservice.service.MessageListService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MessageListServiceImpl implements MessageListService {
  private final MessageListRepository messageListRepository;
  private final ModelMapper modelMapper;
  
  public MessageListServiceImpl(MessageListRepository messageListRepository) {
    this.messageListRepository = messageListRepository;
    modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
      .setFieldMatchingEnabled(true)
      .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
  }
  
  @Override
  public List<MessageListDTO> getALl() {
    return messageListRepository.findAll().stream().map(messageList -> {
      
      log.debug("{}", messageList.getParticipants());
      
      return modelMapper.map(messageList, MessageListDTO.class);
    }).collect(Collectors.toList());
  }
  
  @Override
  public MessageListDTO getMessagesListByParticipant(ParticipantDTO participantDTO) {
    Participant participant = modelMapper.map(participantDTO, Participant.class);
    MessageList messageList = messageListRepository.findFirstByParticipantsContains(participant).orElseThrow();
    log.debug("MESSAGGI LISTA from/to {} - {}", participant, messageList);
    return modelMapper.map(messageList, MessageListDTO.class);
  }
}
