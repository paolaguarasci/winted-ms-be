package it.pingflood.winted.messageservice.data.dto;

import it.pingflood.winted.messageservice.data.MsgType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageResponse {
  private String id;
  private String from;
  private String to;
  private String content;
  private String timestamp;
  private String timeAgo;
  private MsgType messageType;
}
