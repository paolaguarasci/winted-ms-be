package it.pingflood.winted.paymentservice.repository;

import it.pingflood.winted.paymentservice.data.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, UUID>, JpaSpecificationExecutor<PaymentMethod> {
  
  List<PaymentMethod> findByUser(String userid);
  
  Optional<PaymentMethod> findFirstByUser(String userid);
  
}
