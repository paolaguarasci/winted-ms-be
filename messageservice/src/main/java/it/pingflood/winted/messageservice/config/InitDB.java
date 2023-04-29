package it.pingflood.winted.messageservice.config;

import it.pingflood.winted.messageservice.data.Message;
import it.pingflood.winted.messageservice.data.MessageList;
import it.pingflood.winted.messageservice.data.Participants;
import it.pingflood.winted.messageservice.repository.MessageListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
      List<Participants> participants = new ArrayList<>();
      participants.add(Participants.builder().id("1").build());
      participants.add(Participants.builder().id("2").build());
      
      List<Message> messages = new ArrayList<>();
      messages.add(Message.builder().content("Ciao!").from("1").to("2").build());
      messages.add(Message.builder().content("Ciao a te!").from("2").to("1").build());
      
      messageListRepository.save(MessageList.builder().messages(messages).participants(participants).build());
      log.debug("Lista salvata");
    }
    log.debug("Termino l'init dei dati");
  }
  
}
