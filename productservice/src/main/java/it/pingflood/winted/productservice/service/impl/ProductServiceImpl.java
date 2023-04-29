package it.pingflood.winted.productservice.service.impl;

import it.pingflood.winted.productservice.data.Product;
import it.pingflood.winted.productservice.data.dto.ProductPutRequest;
import it.pingflood.winted.productservice.data.dto.ProductRequest;
import it.pingflood.winted.productservice.data.dto.ProductResponse;
import it.pingflood.winted.productservice.repository.ProductRepository;
import it.pingflood.winted.productservice.service.ProductService;
import jakarta.ws.rs.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    return modelMapper.map(productRepository.findById(id).orElseThrow(), ProductResponse.class);
  }
  
  @Override
  public List<ProductResponse> getAll() {
    return productRepository.findAll().stream().map(product -> modelMapper.map(product, ProductResponse.class)).toList();
  }
  
  @Override
  public ProductResponse createProduct(ProductRequest productRequest) {
    Product newProduct = Product.builder().name(productRequest.getName()).description(productRequest.getDescription()).price(productRequest.getPrice()).build();
    
    log.debug("SERVICE - Richiesta nuovo prodotto {}", productRequest);
    log.debug("Salvo il nuovo prodotto {}", newProduct);
    
    return modelMapper.map(productRepository.save(newProduct), ProductResponse.class);
  }
  
  @Override
  public ProductResponse updateProduct(String id, ProductPutRequest productPutRequest) {
    if (!productPutRequest.getId().equals(id)) throw new BadRequestException();
    Product oldProduct = productRepository.findById(id).orElseThrow();
    
    // TODO Sostituire con una iterazione dinamica (se poi cambiano i fields??)
    oldProduct.setName(productPutRequest.getName());
    oldProduct.setDescription(productPutRequest.getDescription());
    oldProduct.setPrice(productPutRequest.getPrice());
    
    return modelMapper.map(productRepository.save(oldProduct), ProductResponse.class);
  }
  
  @Override
  public void deleteProduct(String id) {
    productRepository.delete(productRepository.findById(id).orElseThrow());
  }
}
