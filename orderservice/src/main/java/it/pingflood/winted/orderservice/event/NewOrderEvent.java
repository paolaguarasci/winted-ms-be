package it.pingflood.winted.orderservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewOrderEvent {
  private String productId;
  private String buyerId;
  private String sellerId;
}