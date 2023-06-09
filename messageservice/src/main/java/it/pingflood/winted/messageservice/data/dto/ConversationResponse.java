package it.pingflood.winted.messageservice.data.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ConversationResponse implements Serializable {
  private String id;
  private String user1;
  private String user2;
  private String loggedUser;
  private String altroUtente;
  private List<MessageResponse> messages;
  private String prodottoCorrelato;
}
