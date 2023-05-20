package it.pingflood.winted.paymentservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentMethodResponse {
  private String id;
  private String username;
  private String last4Digit;
  private String gestore;
}
