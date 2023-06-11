package it.pingflood.winted.messageservice.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewCheckOutRequest implements Serializable {
  private String product;
  private String buyer;
  private String price;
}
