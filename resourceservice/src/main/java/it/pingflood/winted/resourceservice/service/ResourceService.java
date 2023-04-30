package it.pingflood.winted.resourceservice.service;

import it.pingflood.winted.resourceservice.data.dto.ImageRequest;
import it.pingflood.winted.resourceservice.data.dto.ImageResponse;

import java.util.UUID;

public interface ResourceService {
  ImageResponse getOne(UUID id);
  
  ImageResponse saveOne(ImageRequest imageRequest);
}
