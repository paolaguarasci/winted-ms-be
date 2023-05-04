package it.pingflood.winted.productservice.service.impl;

import it.pingflood.winted.productservice.data.Product;
import it.pingflood.winted.productservice.data.dto.ProductPutRequest;
import it.pingflood.winted.productservice.data.dto.ProductRequest;
import it.pingflood.winted.productservice.data.dto.ProductResponse;
import it.pingflood.winted.productservice.repository.ProductRepository;
import it.pingflood.winted.productservice.service.ProductService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService {
  
  private final ProductRepository productRepository;
  private final ModelMapper modelMapper;
  public static final String EXTERNAL_UPLOAD_URL = "http://localhost:8004/api/v1/resource/image";
  
  public ProductServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
    modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
      .setFieldMatchingEnabled(true)
      .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
  }
  
  @Override
  public ProductResponse getOne(String id) {
    ObjectId objectId = new ObjectId(id);
    return modelMapper.map(productRepository.findById(objectId.toString()).orElseThrow(), ProductResponse.class);
  }
  
  @Override
  public List<ProductResponse> getAll() {
    return productRepository.findAll().stream().map(product -> modelMapper.map(product, ProductResponse.class)).toList();
  }
  
  @SneakyThrows
  @Override
  public ProductResponse createProduct(ProductRequest productRequest) {
    Product newProduct = Product.builder().name(productRequest.getName()).description(productRequest.getDescription()).price(productRequest.getPrice()).build();
    Product savedProduct = productRepository.save(newProduct);
    
    MultipartBodyBuilder builder = new MultipartBodyBuilder();
    String headerFile = String.format("form-data; name=%s; filename=%s", "file", productRequest.getFile().getOriginalFilename());
    builder.part("file", new ByteArrayResource(productRequest.getFile().getBytes()), MediaType.IMAGE_PNG).header("Content-Disposition", headerFile);
    
    MultiValueMap<String, HttpEntity<?>> multipartBody = builder.build();
    
    String resp = WebClient.builder().build().post()
      .uri(EXTERNAL_UPLOAD_URL)
      .contentType(MediaType.MULTIPART_FORM_DATA)
      .body(BodyInserters.fromMultipartData(multipartBody))
      .exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class)).block();
    
    log.debug("Risposta dal servizio di upload immagini\n{}", resp);
    
    return modelMapper.map(savedProduct, ProductResponse.class);
  }
  
  @SneakyThrows
  @Override
  public ProductResponse updateProduct(String id, ProductPutRequest productPutRequest) {
    ObjectId objectId = new ObjectId(id);
    if (!productPutRequest.getId().equals(objectId.toString())) throw new IllegalArgumentException();
    Product oldProduct = productRepository.findById(objectId.toString()).orElseThrow();
    
    // TODO Sostituire con una iterazione dinamica (se poi cambiano i fields??)
    oldProduct.setName(productPutRequest.getName());
    oldProduct.setDescription(productPutRequest.getDescription());
    oldProduct.setPrice(productPutRequest.getPrice());
    
    return modelMapper.map(productRepository.save(oldProduct), ProductResponse.class);
  }
  
  @Override
  public void deleteProduct(String id) {
    ObjectId objectId = new ObjectId(id);
    productRepository.delete(productRepository.findById(objectId.toString()).orElseThrow());
  }
}
