package it.pingflood.winted.paymentservice.data.dto;


import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
public class TransactionRequest implements Serializable {
  private String paymentMethodId;
  private String from;
  private String to;
  private String importo;
}
