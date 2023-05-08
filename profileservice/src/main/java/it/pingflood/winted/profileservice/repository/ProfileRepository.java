package it.pingflood.winted.profileservice.repository;

import it.pingflood.winted.profileservice.data.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends MongoRepository<Profile, String> {
}
