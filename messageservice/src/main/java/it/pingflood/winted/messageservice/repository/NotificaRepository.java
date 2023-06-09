package it.pingflood.winted.messageservice.repository;

import it.pingflood.winted.messageservice.data.Notifica;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificaRepository extends MongoRepository<Notifica, String> {
  List<Notifica> findAllByUser(String user);
}
