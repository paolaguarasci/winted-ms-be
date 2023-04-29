package it.pingflood.winted.productservice.config;

import it.pingflood.winted.productservice.data.Product;
import it.pingflood.winted.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class InitDb implements CommandLineRunner {
  private final ProductRepository productRepository;
  
  public void run(String... args) throws Exception {
    if (productRepository.count() < 1) {
      productRepository.save(Product.builder().name("Levis Jeans").description("Levis Jeans").price(BigDecimal.valueOf(35)).build());
    }
  }
  
}
