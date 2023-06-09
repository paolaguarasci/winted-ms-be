package it.pingflood.winted.messageservice.data.dto;

import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class MessageResponse implements Serializable {
  private String id;
  private String from;
  private String to;
  private String content;
  private String timestamp;
  private String timeAgo;
  private String messageType;
}
