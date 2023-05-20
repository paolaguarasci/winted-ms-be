package it.pingflood.winted.messageservice.data;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "message")
public class Message {
  private String id;
  private String from;
  private String to;
  private LocalDateTime timestamp;
  private String content;
  private String answer;
  private String timeAgo;
  private MsgType messageType;
}
