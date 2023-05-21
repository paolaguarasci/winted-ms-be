package it.pingflood.winted.messageservice.config;

import it.pingflood.winted.messageservice.data.Conversation;
import it.pingflood.winted.messageservice.data.Message;
import it.pingflood.winted.messageservice.data.MsgType;
import it.pingflood.winted.messageservice.repository.ConversationRepository;
import it.pingflood.winted.messageservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile(value = "dev")
public class InitDB implements CommandLineRunner {
  private final MessageRepository messageRepository;
  private final ConversationRepository conversationRepository;
  
  public void run(String... args) throws Exception {
    log.debug("Init dei dati");
    if (conversationRepository.count() == 0L) {
      log.debug("Salvo una lista messaggi di prova");
      
      List<Message> messages = new ArrayList<>();
      
      String user1 = "6464d3155ded8d052d323c2a";
      String user2 = "6464db0bf96f9d64c2a2787b";
      
      messages.add(Message.builder()
        .content("Ciao!")
        .timestamp(LocalDateTime.now())
        .from(user1)
        .to(user2)
        .messageType(MsgType.TESTO)
        .build());
      messages.add(Message.builder()
        .content("Ciao a te!")
        .timestamp(LocalDateTime.now())
        .from(user2)
        .to(user1)
        .messageType(MsgType.TESTO)
        .build());
      messages.add(Message.builder()
        .content("Come va?")
        .timestamp(LocalDateTime.now())
        .from(user1)
        .to(user2)
        .messageType(MsgType.TESTO)
        .build());
      messages.add(Message.builder()
        .content("Molto Bene!")
        .timestamp(LocalDateTime.now())
        .from(user2)
        .to(user1)
        .messageType(MsgType.TESTO)
        .build());
      messageRepository.saveAll(messages);
      
      conversationRepository.save(Conversation.builder()
        .user1(user1)
        .user2(user2)
        .messages(messages)
        .build());
      
      
      log.debug("Lista salvata");
    }
    log.debug("Termino l'init dei dati");
  }
  
}
