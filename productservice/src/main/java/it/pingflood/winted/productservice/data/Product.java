package it.pingflood.winted.productservice.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@Document(value = "product")
@Data
public class Product {
  @Id
  private String id;
  @Indexed(unique = true)
  private String name;
  private String description;
  private BigDecimal price;
  private List<ResourceLink> resources = new ArrayList<>();
}
