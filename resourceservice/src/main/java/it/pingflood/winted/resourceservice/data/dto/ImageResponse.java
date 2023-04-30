package it.pingflood.winted.resourceservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ImageResponse {
  String id;
  String productRelated;
  String isFeatured;
  String fileName;
  String fileType;
  String fileUrl;
}
