package it.pingflood.winted.resourceservice.service.impl;

import it.pingflood.winted.resourceservice.data.Image;
import it.pingflood.winted.resourceservice.data.dto.ImageRequest;
import it.pingflood.winted.resourceservice.data.dto.ImageResponse;
import it.pingflood.winted.resourceservice.repository.ImageRepository;
import it.pingflood.winted.resourceservice.service.ResourceService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

@Service
@Slf4j
public class ResourceServiceImpl implements ResourceService {
  
  private static final String DIR_TO_UPLOAD = "./resourceservice/uploads/";
  private final ModelMapper modelMapper;
  private final ImageRepository imageRepository;
  
  public ResourceServiceImpl(ImageRepository imageRepository) {
    this.imageRepository = imageRepository;
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
    
    saveToFile(imageRequest.getFile().getBytes(), imageRequest.getFile().getOriginalFilename());
    
    return modelMapper.map(imageRepository.save(modelMapper.map(imageRequest, Image.class)), ImageResponse.class);
  }
  
  
  private void saveToFile(byte[] bytes, String filename) {
    Path path = Paths.get(DIR_TO_UPLOAD + filename);
    try {
      Files.write(path, bytes, StandardOpenOption.CREATE_NEW);
    } catch (IOException e) {
      // TODO Definire una eccezione migliore
      throw new RuntimeException(e);
    }
  }
}
