package it.pingflood.winted.messageservice.data.dto;

import it.pingflood.winted.messageservice.data.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConversationRequest implements Serializable {
//  private String id;
  private String user1;
  private String user2;
  private String loggedUser;
  private String altroUtente;
  private List<Message> messages;
  private String prodottoCorrelato;
}
