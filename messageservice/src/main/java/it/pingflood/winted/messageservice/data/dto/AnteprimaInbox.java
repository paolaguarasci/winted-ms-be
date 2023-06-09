package it.pingflood.winted.messageservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AnteprimaInbox implements Serializable {
  String altroUtente;
  String timeAgo;
  String lastMessage;
  String prodottoCorrelato;
  String conversationId;
}
