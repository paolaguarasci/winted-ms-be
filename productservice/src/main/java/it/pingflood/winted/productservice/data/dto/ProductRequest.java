package it.pingflood.winted.productservice.data.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
public class ProductRequest implements Serializable {
  private String name;
  private String description;
  private BigDecimal price;
  private String brand;
  private String category;
  private MultipartFile[] files;
  private boolean draft;
}
