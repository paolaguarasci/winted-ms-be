package it.pingflood.winted.messageservice.data.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
public class MessageListResponse implements Serializable {
  private String altroUtente;
  private String prodottoCorrelato;
  private List<MessageResponse> messaggi;
}
