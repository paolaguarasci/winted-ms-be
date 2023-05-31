package it.pingflood.winted.profileservice.repository;

import it.pingflood.winted.profileservice.data.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends MongoRepository<Profile, String>, QuerydslPredicateExecutor<Profile> {
  Optional<Profile> findAllByUsername(String username);
  
  Optional<Profile> findByProviderIdentityId(String id);
  
  Optional<Object> findByUsername(String username);
}
