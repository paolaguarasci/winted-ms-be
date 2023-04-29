package it.pingflood.winted.messageservice.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(value = "message-list")
@Builder
@AllArgsConstructor
@Data
public class MessageList {
  @Id
  private String id;
  private List<Participants> participants;
  private List<Message> messages;
}
