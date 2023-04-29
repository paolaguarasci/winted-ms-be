package it.pingflood.winted.orderservice.data.dto;

import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderResponse implements Serializable {
  private Long id;
  private String user;
  private String product;
}
