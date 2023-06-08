package it.pingflood.winted.orderservice.client.data;

import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
public class PaymentResponse implements Serializable {
  private String id;
  private String paymentMethodId;
  private String from;
  private String to;
  private String importo;
  private String status;
}
