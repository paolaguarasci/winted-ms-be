package it.pingflood.winted.orderservice.repository;

import it.pingflood.winted.orderservice.data.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
  Optional<Order> findById(UUID id);
  
  List<Order> findAllByUser(String user);
  
  Optional<Order> findByUserAndProduct(String user, String product);
}
