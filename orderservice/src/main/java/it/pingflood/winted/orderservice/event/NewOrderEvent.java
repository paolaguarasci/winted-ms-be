package it.pingflood.winted.orderservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewOrderEvent implements Serializable {
  private String productId;
  private String buyerId;
  private String sellerId;
}
