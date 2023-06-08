package it.pingflood.winted.paymentservice.data.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionResponse implements Serializable {
  private String id;
  private String paymentMethodId;
  private String paymentFrom;
  private String paymentTo;
  private Double paymentImport;
  private String status;
}
