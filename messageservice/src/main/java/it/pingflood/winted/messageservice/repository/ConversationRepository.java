package it.pingflood.winted.messageservice.repository;

import it.pingflood.winted.messageservice.data.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation, String> {
  List<Conversation> findAllByUser1IsOrUser2Is(String user1, String user2);
}
