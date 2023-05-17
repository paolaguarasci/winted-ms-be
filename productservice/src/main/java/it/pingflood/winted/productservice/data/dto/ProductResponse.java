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
  private Integer preferred;
  private String brand_id; // brand-id
  private String category_id;
  private String owner_id; // owner-id
  private String featured;
  private List<String> resources;
}
