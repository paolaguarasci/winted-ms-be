package it.pingflood.winted.messageservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewOrderEvent implements Serializable {
  private String product;
  private String buyer;
  private String seller;
}
