package it.pingflood.winted.messageservice.data;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(value = "message-list")
@Builder
@Getter
@Setter
@AllArgsConstructor
@Data
public class MessageList {
  @Id
  private String id;
  private List<Participant> participants = new ArrayList<>();
  private List<Message> messages = new ArrayList<>();
}
