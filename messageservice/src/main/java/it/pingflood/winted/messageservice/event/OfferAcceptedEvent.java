package it.pingflood.winted.messageservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferAcceptedEvent {
  private String productId;
  private String buyerId;
  private String sellerId;
  private String price;
  private Boolean isAccepted;
}
