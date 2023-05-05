package it.pingflood.winted.productservice.controller;

import it.pingflood.winted.productservice.data.dto.ProductPutRequest;
import it.pingflood.winted.productservice.data.dto.ProductRequest;
import it.pingflood.winted.productservice.data.dto.ProductResponse;
import it.pingflood.winted.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/product")
@Slf4j
public class ProductController {
  public final ProductService productService;
  
  public ProductController(ProductService productService) {
    this.productService = productService;
  }
  
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<List<ProductResponse>> getAll() {
    // FIXME Ha senso fare l'optional di una lista?
    return ResponseEntity.of(Optional.of(productService.getAll()));
  }
  
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ProductResponse> getOne(@PathVariable("id") String id) {
    return ResponseEntity.of(Optional.of(productService.getOne(id)));
  }

//  @PostMapping
//  @ResponseStatus(HttpStatus.CREATED)
//  public ResponseEntity<ProductResponse> createOne(@RequestBody ProductRequest productRequest) {
//    log.debug("CONTROLLER - Richiesta nuovo prodotto {}", productRequest);
//    return ResponseEntity.of(Optional.of(productService.createProduct(productRequest)));
//  }
  
  
  @PostMapping(consumes = {"multipart/form-data"})
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ProductResponse> saveImage(ProductRequest productRequest) {
    log.debug("CONTROLLER - Richiesta nuovo prodotto {}", productRequest);
    return ResponseEntity.of(Optional.of(productService.createProduct(productRequest)));
  }
  
  
  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ProductResponse> updateOne(@PathVariable("id") String id, @RequestBody ProductPutRequest productRequest) {
    return ResponseEntity.of(Optional.of(productService.updateProduct(id, productRequest)));
  }
  
  
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteOne(@PathVariable("id") String id) {
    productService.deleteProduct(id);
  }
  
}
