package it.pingflood.winted.orderservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderPutRequest  implements Serializable {
  private Long id;
  private String user;
  private String product;
}
