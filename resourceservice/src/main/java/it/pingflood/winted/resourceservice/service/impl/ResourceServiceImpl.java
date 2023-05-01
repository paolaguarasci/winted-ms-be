package it.pingflood.winted.resourceservice.service.impl;

import com.amazonaws.services.s3.model.S3Object;
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
  
  @SneakyThrows
  @Override
  public byte[] getOne(UUID id) {
    Image imgMeta = imageRepository.findById(id).orElseThrow();
    S3Object s3Obj = awsService.getObj(imgMeta.getFileName());
    return s3Obj.getObjectContent().readAllBytes();
  }
  
  @SneakyThrows
  @Override
  public ImageResponse saveOne(ImageRequest imageRequest) {
    String key = awsService.saveObj(imageRequest.getFile().getBytes());
    log.debug("Immagine caricata su {}", awsService.getObjectUrl(key));
    Image img = modelMapper.map(imageRequest, Image.class);
    img.setFileUrl(awsService.getObjectUrl(key));
    img.setFileName(key);
    return modelMapper.map(imageRepository.save(img), ImageResponse.class);
  }
}
