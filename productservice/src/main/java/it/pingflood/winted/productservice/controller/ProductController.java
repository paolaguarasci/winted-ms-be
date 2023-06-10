package it.pingflood.winted.productservice.controller;

import it.pingflood.winted.productservice.data.dto.ProductPutRequest;
import it.pingflood.winted.productservice.data.dto.ProductRequest;
import it.pingflood.winted.productservice.data.dto.ProductResponse;
import it.pingflood.winted.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
  public ResponseEntity<List<ProductResponse>> getAll(@RequestParam(required = false) String owner, @RequestParam(required = false) String sameto, Principal principal) {
    
    if (principal != null) {
      log.info("Logged user {}", principal.getName());
    }
    
    if (owner != null && !owner.isBlank() && principal != null && owner.equals(principal.getName())) {
      return ResponseEntity.of(Optional.of(productService.getAllByOwnerId(owner)));
    }
    
    if (owner != null && !owner.isBlank()) {
      return ResponseEntity.of(Optional.of(productService.getAllPublicByOwnerId(owner)));
    }
    
    if (sameto != null && !sameto.isBlank()) {
      return ResponseEntity.of(Optional.of(productService.getSameByProdId(sameto)));
    }
    
    return ResponseEntity.of(Optional.of(productService.getAll()));
  }
  
  @GetMapping("/search")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<List<ProductResponse>> search(@RequestParam("s") String query) {
    log.info("Search query {}", query);
    return ResponseEntity.of(Optional.of(productService.search(query)));
  }

//  @GetMapping
//  @ResponseStatus(HttpStatus.OK)
//  public ResponseEntity<List<ProductResponse>> getAllSearch() {
//    return ResponseEntity.ok(productService.getAll());
//  }
  
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
  public ResponseEntity<ProductResponse> saveImage(ProductRequest productRequest, Principal principal, @AuthenticationPrincipal Jwt token) {
    log.debug("CONTROLLER - Richiesta nuovo prodotto {} - owner {}", productRequest, principal.getName());
    return ResponseEntity.of(Optional.of(productService.createProduct(productRequest, principal.getName(), token.getTokenValue())));
  }
  
  
  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ProductResponse> updateOne(@PathVariable("id") String id, @RequestBody ProductPutRequest productRequest) {
    return ResponseEntity.of(Optional.of(productService.updateProduct(id, productRequest)));
  }
  
  @PostMapping("/{id}/bought")
//  @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ProductResponse> makeBought(@PathVariable String id) {
    return ResponseEntity.of(Optional.of(productService.makeBought(id)));
  }
  
  @PostMapping("/{id}/bought/undo")
//  @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ProductResponse> undoBought(@PathVariable String id) {
    return ResponseEntity.of(Optional.of(productService.undoBought(id)));
  }
  
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteOne(@PathVariable("id") String id, Principal principal) {
    productService.deleteProduct(id, principal.getName());
  }
  
}
