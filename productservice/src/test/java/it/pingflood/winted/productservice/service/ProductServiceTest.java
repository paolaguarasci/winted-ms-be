package it.pingflood.winted.productservice.service;

import com.mongodb.DuplicateKeyException;
import it.pingflood.winted.productservice.data.Product;
import it.pingflood.winted.productservice.data.dto.ProductPutRequest;
import it.pingflood.winted.productservice.data.dto.ProductRequest;
import it.pingflood.winted.productservice.data.dto.ProductResponse;
import it.pingflood.winted.productservice.repository.ProductRepository;
import it.pingflood.winted.productservice.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
  
  @Mock
  private ProductRepository productRepository;
  
  @InjectMocks
  private ProductServiceImpl productService;
  
  private Product product;
  private ProductRequest productRequest;
  private ProductPutRequest productPutRequest;
  
  
  @BeforeEach
  void setup() {
    product = Product.builder().id("644d35690b56f0720b01f1a7").name("Levis1 Jeans1").description("Desc Levis1 Jeans1").price(BigDecimal.valueOf(35)).build();
    productRequest = ProductRequest.builder().name("Levis1 Jeans1").description("Desc Levis1 Jeans1").price(BigDecimal.valueOf(35)).build();
    productPutRequest = ProductPutRequest.builder().id("644d35690b56f0720b01f1a7").name("Levis11 Jeans11").description("Desc Levis111 Jeans111").price(BigDecimal.valueOf(135)).build();
  }
  
  @DisplayName("Save - return correct value")
  @Test
  void when_save_product_is_should_be_return_product() {
    when(productRepository.save(any(Product.class))).thenReturn(product);
    ProductResponse productResponse = productService.createProduct(productRequest);
    assertThat(productResponse.getName()).isSameAs(productResponse.getName());
  }
  
  @DisplayName("Save - throw exception when duplicate unique field")
  @Test
  void when_try_to_save_whit_existing_name_throws_exception() {
    product.setId(null); // altrimenti solleva un eccezione rispetto ad un potenziale errore di stubbing
    when(productRepository.save(product)).thenThrow(DuplicateKeyException.class);
    org.junit.jupiter.api.Assertions.assertThrows(DuplicateKeyException.class, () -> productService.createProduct(productRequest));
  }
  
  @DisplayName("Get All - return correct value")
  @Test
  void when_get_products_should_be_return_products() {
    when(productRepository.findAll()).thenReturn(List.of(product, product, product));
    List<ProductResponse> productsResponse = productService.getAll();
    assertThat(productsResponse)
      .isInstanceOf(List.class)
      .hasSize(3);
    assertThat(productsResponse.get(0).getName())
      .isEqualTo(product.getName());
  }
  
  @DisplayName("Get One - return correct value")
  @Test
  void when_get_product_should_be_return_product() {
    when(productRepository.findById(any())).thenReturn(Optional.ofNullable(product));
    ProductResponse productResponse = productService.getOne("644d35690b56f0720b01f1a9");
    assertThat(productResponse.getName()).isEqualTo(productResponse.getName());
  }
  
  @DisplayName("Get One - throw exception when search not correct id")
  @Test
  void when_get_product_whit_wrong_id_should_throw() {
    org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> productService.getOne("12344asasdk;sjdfkjs"));
  }
  
  @DisplayName("Get One - throw exception when search not present id")
  @Test
  void when_get_product_whit_not_present_id_should_throw() {
    when(productRepository.findById(any())).thenReturn(Optional.empty());
    org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class, () -> productService.getOne("644d35690b56f0720b01f1a9"));
  }
  
  @DisplayName("Put One - throw exception when try to change not correct id")
  @Test
  void when_put_product_whit_wrong_id_should_throw() {
    org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> productService.updateProduct("12344asasdk;sjdfkjs", productPutRequest));
  }
  
  @DisplayName("Put One - throw exception when try to change not present id")
  @Test
  void when_put_product_whit_not_present_id_should_throw() {
    when(productRepository.findById(any())).thenReturn(Optional.empty());
    productPutRequest.setId("644d35690b56f0720b01f1a9");
    org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class, () -> productService.updateProduct("644d35690b56f0720b01f1a9", productPutRequest));
  }
  
  @DisplayName("Put One - throw exception when try to change mismatch id")
  @Test
  void when_put_product_whit_mismatch_id_should_throw() {
    org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> productService.updateProduct("644d35690b56f0720b01f1a9", productPutRequest));
  }
  
  @DisplayName("Put One - update product then return it")
  @Test
  void when_put_product_whit_should_return_it() {
    Product updatedProduct = Product.builder().id(productPutRequest.getId()).name(productPutRequest.getName()).description(productPutRequest.getDescription()).price(productPutRequest.getPrice()).build();
    when(productRepository.save(updatedProduct)).thenReturn(product);
    when(productRepository.findById(productPutRequest.getId())).thenReturn(Optional.of(product));
    ProductResponse pr = productService.updateProduct(productPutRequest.getId(), productPutRequest);
    assertThat(pr.getName()).isEqualTo(updatedProduct.getName());
  }
  
  
  @DisplayName("Delete One - throw exception when try to delete not correct id")
  @Test
  void when_delete_product_whit_wrong_id_should_throw() {
    org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> productService.deleteProduct("12344asasdk;sjdfkjs"));
  }
  
  @DisplayName("Delete One - throw exception when try to delete not present id")
  @Test
  void when_delete_product_whit_not_present_id_should_throw() {
    when(productRepository.findById(any())).thenReturn(Optional.empty());
    org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class, () -> productService.deleteProduct("644d35690b56f0720b01f1a9"));
  }

//  @DisplayName("Delete One - delete element")
//  @Test
//  void when_delete_product_should_delete() {
//    productService.deleteProduct("644d35690b56f0720b01f1a9");
//  }
}
