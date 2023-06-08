package it.pingflood.winted.orderservice.client.data;

import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
public class PaymentRequest implements Serializable {
  private String paymentMethodId;
  private String from;
  private String to;
  private String importo;
}
