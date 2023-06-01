package it.pingflood.winted.addressservice.repository;

import it.pingflood.winted.addressservice.data.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID>, JpaSpecificationExecutor<Address> {
  Optional<Address> findFirstByUser(String user);
  
  List<Address> findAllByUser(String user);
  
}
