package it.pingflood.winted.productservice.service;

import it.pingflood.winted.productservice.data.dto.ProductPutRequest;
import it.pingflood.winted.productservice.data.dto.ProductRequest;
import it.pingflood.winted.productservice.data.dto.ProductResponse;

import java.util.List;

public interface ProductService {
  
  ProductResponse getOne(String id);
  
  List<ProductResponse> getAll();
  
  List<ProductResponse> search(String query);
  
  ProductResponse createProduct(ProductRequest productRequest, String owner, String token);
  
  ProductResponse updateProduct(String id, ProductPutRequest productPutRequest);
  
  void deleteProduct(String id);
  
  void addPreferred(String productId);
  
  void removePreferred(String productId);
  
  List<ProductResponse> getAllByOwnerId(String ownerId);
  
  List<ProductResponse> getAllPublicByOwnerId(String ownerId);
  
  List<ProductResponse> getSameByProdId(String prodid);
  
  ProductResponse makeBought(String productId);
  
  ProductResponse undoBought(String productId);
}
