package it.pingflood.winted.orderservice.client.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentMethodResponse implements Serializable {
  private String id;
  private String user;
  private String last4Digit;
  private String gestore;
}
