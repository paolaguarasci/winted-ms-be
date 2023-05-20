package it.pingflood.winted.productservice.config;

import it.pingflood.winted.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitDb implements CommandLineRunner {
  private final ProductRepository productRepository;
  
  public void run(String... args) throws Exception {
    if (productRepository.count() < 1) {
//      productRepository.save(Product.builder().name("Levis1 Jeans1").description("Desc Levis1 Jeans1").price(BigDecimal.valueOf(35)).build());
//      productRepository.save(Product.builder().name("Levis2 Jeans2").description("Desc Levis2 Jeans2").price(BigDecimal.valueOf(75)).build());
//      productRepository.save(Product.builder().name("Levis3 Jeans3").description("Desc Levis3 Jeans3").price(BigDecimal.valueOf(120)).build());
    }
  }
  
}
