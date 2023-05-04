package it.pingflood.winted.productservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductResponse {
  private String id;
  private String name;
  private String description;
  private BigDecimal price;
  private List<String> resources;
}
