package it.pingflood.winted.resourceservice.service.impl;

import it.pingflood.winted.resourceservice.data.Image;
import it.pingflood.winted.resourceservice.data.dto.ImageRequest;
import it.pingflood.winted.resourceservice.data.dto.ImageResponse;
import it.pingflood.winted.resourceservice.repository.ImageRepository;
import it.pingflood.winted.resourceservice.service.AWSService;
import it.pingflood.winted.resourceservice.service.ResourceService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ResourceServiceImpl implements ResourceService {
  private final ModelMapper modelMapper;
  private final ImageRepository imageRepository;
  private final AWSService awsService;
  
  public ResourceServiceImpl(ImageRepository imageRepository, AWSService awsService) {
    this.imageRepository = imageRepository;
    this.awsService = awsService;
    this.modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
      .setFieldMatchingEnabled(true)
      .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
  }
  
  @Override
  public ImageResponse getOne(UUID id) {
    return modelMapper.map(imageRepository.findById(id).orElseThrow(), ImageResponse.class);
  }
  
  @SneakyThrows
  @Override
  public ImageResponse saveOne(ImageRequest imageRequest) {
    String url = awsService.saveObj(imageRequest.getFile().getBytes());
    log.debug("Immagine caricata su {}", url);
    Image img = modelMapper.map(imageRequest, Image.class);
    img.setFileUrl(url);
    return modelMapper.map(imageRepository.save(img), ImageResponse.class);
  }
}
