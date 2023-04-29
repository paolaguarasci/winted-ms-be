package it.pingflood.winted.messageservice.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {
  private String id;
  private String from;
  private String to;
  private String content;
  private String answer;
}
