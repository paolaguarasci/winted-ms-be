package it.pingflood.winted.messageservice.data;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {
  private String id;
  private String from;
  private String to;
  private LocalDateTime timestamp;
  private String content;
  private String answer;
}
