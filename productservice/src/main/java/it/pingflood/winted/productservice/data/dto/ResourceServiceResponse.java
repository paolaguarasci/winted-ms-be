package it.pingflood.winted.productservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResourceServiceResponse implements Serializable {
  String id;
  String productRelated;
  String isFeatured;
  String fileName;
  String fileType;
  String fileUrl;
}
