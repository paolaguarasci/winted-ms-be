package it.pingflood.winted.paymentservice.data.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionRequest {
  private String paymentMethodId;
  private String from;
  private String to;
  private String importo;
}
