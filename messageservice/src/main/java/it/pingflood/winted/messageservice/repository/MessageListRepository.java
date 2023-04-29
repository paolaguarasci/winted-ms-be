package it.pingflood.winted.messageservice.repository;

import it.pingflood.winted.messageservice.data.MessageList;
import it.pingflood.winted.messageservice.data.Participant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageListRepository extends MongoRepository<MessageList, String> {


//  List<MessageList> findAllByParticipantsIsContainingIgnoreCase(Participant participant);
  
  
  Optional<MessageList> findFirstByParticipantsContains(Participant participant);
}
