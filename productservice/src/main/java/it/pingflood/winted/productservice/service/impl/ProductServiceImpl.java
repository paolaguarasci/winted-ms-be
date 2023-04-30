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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService {
  
  private final ProductRepository productRepository;
  private final ModelMapper modelMapper;
  
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
    return productRepository.findAll().stream().map(product -> modelMapper.map(product, ProductResponse.class)).collect(Collectors.toList());
  }
  
  @Override
  public ProductResponse createProduct(ProductRequest productRequest) {
    Product newProduct = Product.builder().name(productRequest.getName()).description(productRequest.getDescription()).price(productRequest.getPrice()).build();
    Product savedProduct = productRepository.save(newProduct);
    
    log.debug("SERVICE - Richiesta nuovo prodotto {}", productRequest);
    log.debug("Salvo il nuovo prodotto {}", newProduct);
    log.debug("Prodotto salvato {}", savedProduct);
    
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
