package it.pingflood.winted.productservice.controller;

import it.pingflood.winted.productservice.service.ProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
  public final ProductService productService;
  
  public ProductController(ProductService productService) {
    this.productService = productService;
  }
  
  
}
