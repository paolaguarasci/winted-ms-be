package it.pingflood.winted.messageservice.repository;
import it.pingflood.winted.messageservice.data.Message;
import it.pingflood.winted.messageservice.data.MessageList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageListRepository extends MongoRepository<MessageList, String> {
  @Override
  long count();
}
