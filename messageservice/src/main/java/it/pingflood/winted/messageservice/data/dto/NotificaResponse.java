package it.pingflood.winted.messageservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NotificaResponse {
  private String id;
  private String user;
  private LocalDateTime timestamp;
  private String content;
  private String timeAgo;
  private String prodottoCorrelato;
  private Boolean read;
}
