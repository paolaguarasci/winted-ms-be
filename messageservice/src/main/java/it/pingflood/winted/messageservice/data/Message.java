package it.pingflood.winted.messageservice.data;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "message")
public class Message extends Auditable<String> implements Serializable {
  private String id;
  private String from;
  private String to;
  private LocalDateTime timestamp;
  private String content;
  private String timeAgo;
  private MsgType messageType;
}
