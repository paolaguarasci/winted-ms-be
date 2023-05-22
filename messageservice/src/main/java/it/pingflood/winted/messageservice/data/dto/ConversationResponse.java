package it.pingflood.winted.messageservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConversationResponse {
  private String id;
  private String user1;
  private String user2;
  private String loggedUser;
  private String altroUtente;
  private List<MessageResponse> messages;
  private String prodottoCorrelato;
}
