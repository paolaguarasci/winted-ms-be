package it.pingflood.winted.resourceservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ImageRequest {
  String productRelated;
  String isFeatured;
  MultipartFile[] files;
}
