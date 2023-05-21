package it.pingflood.winted.messageservice.data;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "conversation")
public class Conversation {
  private String id;
  private String user1;
  private String user2;
  private List<Message> messages;
  private String prodottoCorrelato;
}
