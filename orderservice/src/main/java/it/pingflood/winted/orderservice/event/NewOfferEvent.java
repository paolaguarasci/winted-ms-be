package it.pingflood.winted.orderservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewOfferEvent implements Serializable {
  private String product;
  private String buyer;
  private String seller;
  private String price;
}
