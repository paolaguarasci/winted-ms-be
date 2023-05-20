package it.pingflood.winted.messageservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AnteprimaInbox {
  String altroUtente;
  String timeAgo;
  String lastMessage;
  String prodottoCorrelato;
  String conversationId;
}
