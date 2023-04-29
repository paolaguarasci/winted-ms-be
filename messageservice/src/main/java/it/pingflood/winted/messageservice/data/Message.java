package it.pingflood.winted.messageservice.data;

import lombok.*;

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
  private String content;
  private String answer;
}
