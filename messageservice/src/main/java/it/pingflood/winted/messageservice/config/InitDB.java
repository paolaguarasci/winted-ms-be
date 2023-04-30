package it.pingflood.winted.messageservice.config;

import it.pingflood.winted.messageservice.data.Message;
import it.pingflood.winted.messageservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile(value = "dev")
public class InitDB implements CommandLineRunner {
  private final MessageRepository messageRepository;
  
  public void run(String... args) throws Exception {
    log.debug("Init dei dati");
    if (messageRepository.count() == 0L) {
      log.debug("Salvo una lista messaggi di prova");
      List<Message> messages = new ArrayList<>();
      String username1 = "ciccio";
      String username2 = "pippo";
      messages.add(Message.builder().content("Ciao!").from(username1).to(username2).build());
      messages.add(Message.builder().content("Ciao a te!").from(username2).to(username1).build());
      messages.add(Message.builder().content("Come va?").from(username1).to(username2).build());
      messages.add(Message.builder().content("Molto Bene!").from(username2).to(username1).build());
      messageRepository.saveAll(messages);
      log.debug("Lista salvata");
    }
    log.debug("Termino l'init dei dati");
  }
  
}
