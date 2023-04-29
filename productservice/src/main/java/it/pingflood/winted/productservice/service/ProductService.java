package it.pingflood.winted.productservice.service;

import it.pingflood.winted.productservice.data.dto.ProductPutRequest;
import it.pingflood.winted.productservice.data.dto.ProductRequest;
import it.pingflood.winted.productservice.data.dto.ProductResponse;

import java.util.List;

public interface ProductService {
  
  ProductResponse getOne(String id);
  
  List<ProductResponse> getAll();
  
  
  ProductResponse createProduct(ProductRequest productRequest);
  
  ProductResponse updateProduct(String id, ProductPutRequest productPutRequest);
  
  void deleteProduct(String id);
  
}
