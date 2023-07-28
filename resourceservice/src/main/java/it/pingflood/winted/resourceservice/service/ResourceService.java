package it.pingflood.winted.resourceservice.service;

import it.pingflood.winted.resourceservice.data.Resource;
import it.pingflood.winted.resourceservice.data.dto.ImageRequest;
import it.pingflood.winted.resourceservice.data.dto.ImageResponse;

import java.util.List;
import java.util.UUID;

public interface ResourceService {
  byte[] getOne(UUID id);
  
  ImageResponse saveOne(ImageRequest imageRequest);
  
  List<ImageResponse> saveMultiple(ImageRequest imageRequest);
  
  List<Resource> unsafeGetOne(String id);
  List<Resource> unsafeGetOneWithJPA(String id);
}
