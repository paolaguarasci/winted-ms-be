package it.pingflood.winted.messageservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageRequest {
  private String from;
  private String to;
  private String content;
  private String timestamp;
  private String timeAgo;
  private String messageType;
}
