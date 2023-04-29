package it.pingflood.winted.orderservice.config;

import it.pingflood.winted.orderservice.data.Order;
import it.pingflood.winted.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitDB implements CommandLineRunner {
  
  private final OrderRepository orderRepository;
 

  @Override
  public void run(String... args) throws Exception {
    
    if (orderRepository.count() < 1) {
      log.debug("Creazione ordini di esempio");
      
      Order order1 = Order.builder().user("userid1").product("code1").build();
      Order order2 = Order.builder().user("userid2").product("code2").build();
      Order order3 = Order.builder().user("userid3").product("code3").build();
      
      orderRepository.saveAllAndFlush(List.of(order1, order2, order3));
      
      log.debug("Creazione ordini di esempio");
    }
    
  }
}
