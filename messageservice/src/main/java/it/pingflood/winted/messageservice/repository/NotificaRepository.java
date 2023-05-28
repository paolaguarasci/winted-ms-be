package it.pingflood.winted.messageservice.repository;

import it.pingflood.winted.messageservice.data.Notifica;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificaRepository extends MongoRepository<Notifica, String> {
  Optional<Notifica> findAllByUser(String user);
}
