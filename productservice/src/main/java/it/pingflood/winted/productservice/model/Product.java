package it.pingflood.winted.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@Document(value = "product")
@Data
public class Product {
  @Id
  private String id;
  private String name;
  private String description;
  private BigDecimal price;
}
