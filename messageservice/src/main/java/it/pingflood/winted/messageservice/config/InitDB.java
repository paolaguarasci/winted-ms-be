package it.pingflood.winted.messageservice.config;

import it.pingflood.winted.messageservice.data.Message;
import it.pingflood.winted.messageservice.data.MessageList;
import it.pingflood.winted.messageservice.data.Participant;
import it.pingflood.winted.messageservice.repository.MessageListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitDB implements CommandLineRunner {
  private final MessageListRepository messageListRepository;
  
  public void run(String... args) throws Exception {
    log.debug("Init dei dati");
    if (messageListRepository.count() == 0L) {
      log.debug("Salvo una lista messaggi di prova");
      List<Participant> participants = new ArrayList<>();
      participants.add(Participant.builder().username("ciccio").build());
      participants.add(Participant.builder().username("pippo").build());
      
      List<Message> messages = new ArrayList<>();
      messages.add(Message.builder().content("Ciao!").from("ciccio").to("pippo").build());
      messages.add(Message.builder().content("Ciao a te!").from("pippo").to("ciccio").build());
      
      messageListRepository.save(MessageList.builder().messages(messages).participants(participants).build());
      log.debug("Lista salvata");
    }
    log.debug("Termino l'init dei dati");
  }
  
}
