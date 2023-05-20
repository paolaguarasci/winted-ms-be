package it.pingflood.winted.paymentservice.repository;

import it.pingflood.winted.paymentservice.data.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {
}
