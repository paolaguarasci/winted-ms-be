package it.pingflood.winted.messageservice.data;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "conversation")
public class Conversation extends Auditable<String> implements Serializable {
  private String id;
  private String user1;
  private String user2;
  private List<Message> messages;
  private String prodottoCorrelato;
}
