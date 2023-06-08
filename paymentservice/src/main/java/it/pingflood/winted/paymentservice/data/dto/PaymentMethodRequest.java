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
public class PaymentMethodRequest implements Serializable {
  private String gestore;
  private String titolareCarta;
  private String numeroCarta;
  private String dataScadenza;
  private String ccv;
}
