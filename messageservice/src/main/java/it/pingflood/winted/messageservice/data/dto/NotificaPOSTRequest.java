package it.pingflood.winted.messageservice.data.dto;

import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
public class NotificaPOSTRequest implements Serializable {
  private String user;
  private String content;
  private String prodottoCorrelato;
}
