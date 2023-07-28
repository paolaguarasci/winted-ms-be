package it.pingflood.winted.resourceservice.service.impl;

import com.amazonaws.services.s3.model.S3Object;
import it.pingflood.winted.resourceservice.data.Image;
import it.pingflood.winted.resourceservice.data.Resource;
import it.pingflood.winted.resourceservice.data.dto.ImageRequest;
import it.pingflood.winted.resourceservice.data.dto.ImageResponse;
import it.pingflood.winted.resourceservice.repository.ImageRepository;
import it.pingflood.winted.resourceservice.service.AWSService;
import it.pingflood.winted.resourceservice.service.ResourceService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResourceServiceImpl implements ResourceService {
  private final ModelMapper modelMapper;
  private final ImageRepository imageRepository;
  private final AWSService awsService;
  private final DataSource dataSource;
  private final EntityManager em;
  
  public ResourceServiceImpl(ImageRepository imageRepository, AWSService awsService, DataSource dataSource, EntityManager em) {
    this.imageRepository = imageRepository;
    this.awsService = awsService;
    this.dataSource = dataSource;
    this.em = em;
    this.modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
      .setFieldMatchingEnabled(true)
      .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
  }
  
  @SneakyThrows
  @Override
  public byte[] getOne(UUID id) {
    log.info("sono qui 1");
    Image imgMeta = imageRepository.findById(id).orElseThrow();
    log.info("sono qui 2");
    S3Object s3Obj = awsService.getObj(imgMeta.getFileName());
    log.info("sono qui 3");
    return s3Obj.getObjectContent().readAllBytes();
  }
  
  @SneakyThrows
  @Override
  public ImageResponse saveOne(ImageRequest imageRequest) {
    String fileExtension = getExtensionByStringHandling(imageRequest.getFiles()[0].getOriginalFilename()).orElse("");
    String key = awsService.saveObj(imageRequest.getFiles()[0].getBytes(), fileExtension);
    log.debug("Immagine caricata su {}", awsService.getObjectUrl(key));
    Image img = modelMapper.map(imageRequest, Image.class);
    img.setFileUrl(awsService.getObjectUrl(key));
    img.setFileName(key);
    return modelMapper.map(imageRepository.save(img), ImageResponse.class);
  }
  
  @SneakyThrows
  @Override
  public List<ImageResponse> saveMultiple(ImageRequest imageRequest) {
    MultipartFile[] files = imageRequest.getFiles();
    List<ImageResponse> images = new ArrayList<>();
    
    for (MultipartFile file : files) {
      String fileExtension = getExtensionByStringHandling(file.getOriginalFilename()).orElse("");
      String key = awsService.saveObj(file.getBytes(), fileExtension);
      log.debug("Immagine caricata su {}", awsService.getObjectUrl(key));
      Image img = modelMapper.map(imageRequest, Image.class);
      img.setFileUrl(awsService.getObjectUrl(key));
      img.setFileName(key);
      images.add(modelMapper.map(imageRepository.save(img), ImageResponse.class));
    }
    
    
    return images;
  }

  @Override
  public List<Resource> unsafeGetOne(String id) {
    String sql = "select * from resource where id = '" + id + "'";
    log.info("QUERY {}", sql);
    try (Connection c = dataSource.getConnection();
         ResultSet rs = c.createStatement()
           .executeQuery(sql)) {
      List<Resource> resourceList = new ArrayList<>();
      while (rs.next()) {
        Resource resource = Resource.builder()
          .fileUrl(rs.getString("file_url"))
          .fileName(rs.getString("file_name"))
          .fileType(rs.getString("file_type"))
          .productRelated(rs.getString("product_related"))
          .build();
        resourceList.add(resource);
      }
      return resourceList;
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  @Override
  public List<Resource> unsafeGetOneWithJPA(String id) {
    String jpql = "from Resource where id = '" + id + "'";
    TypedQuery<Resource> q = em.createQuery(jpql, Resource.class);
    return q.getResultList();
  }
  
  public Optional<String> getExtensionByStringHandling(String filename) {
    return Optional.ofNullable(filename)
      .filter(f -> f.contains("."))
      .map(f -> f.substring(filename.lastIndexOf(".") + 1));
  }
}
