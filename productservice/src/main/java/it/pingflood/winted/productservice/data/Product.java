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
  private Integer preferred = 0;
  private String brand;
  private String category;
  private String owner;
  private String featured; // resource[0]
  private List<String> resources = new ArrayList<>();
  private boolean bought;
  private boolean draft;
//  private boolean isPublic = false;
  
  public void addResources(String idRes) {
    resources.add(idRes);
  }
  
  public void addPreferred() {
    this.preferred += 1;
  }
  
  public void removePreferred() {
    this.preferred -= 1;
  }
}
