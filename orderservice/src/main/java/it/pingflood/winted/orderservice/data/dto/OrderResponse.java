package it.pingflood.winted.orderservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderResponse implements Serializable {
  private UUID id;
  private String buyer;
  private String product;
  private String status;
  private String owner;
  private String address;
  private String paymentMethod;
  private OffertaResponse offerta;
}
