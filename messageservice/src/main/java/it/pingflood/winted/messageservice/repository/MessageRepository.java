package it.pingflood.winted.messageservice.repository;

import it.pingflood.winted.messageservice.data.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
  Optional<Message> findByFromEqualsIgnoreCaseAndToEqualsIgnoreCaseAndTimestampEquals(String from, String to, LocalDateTime timestamp);
  
  List<Message> findAllByFromIsInAndToIsInOrderByTimestampDesc(List<String> from, List<String> to);
  
  List<Message> findAllByFromIsIgnoreCase(String from);
  
  List<Message> findAllByToIsIgnoreCase(String from);
  
}
