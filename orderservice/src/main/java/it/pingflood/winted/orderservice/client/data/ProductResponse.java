package it.pingflood.winted.orderservice.client.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductResponse implements Serializable {
  private String id;
  private String name;
  private String description;
  private BigDecimal price;
  private Integer preferred;
  private String brand; // brand-id
  private String category;
  private String owner; // owner-id
  private String featured;
  private List<String> resources;
  private String bought;
  private String draft;
}
