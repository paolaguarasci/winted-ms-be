package it.pingflood.winted.productservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductPutRequest {
  private String id;
  private String name;
  private String description;
  private BigDecimal price;
}
