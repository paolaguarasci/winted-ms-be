package it.pingflood.winted.productservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResourceServiceResponse {
  String id;
  String productRelated;
  String isFeatured;
  String fileName;
  String fileType;
  String fileUrl;
}
