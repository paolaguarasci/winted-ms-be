package it.pingflood.winted.profileservice.config;

import it.pingflood.winted.profileservice.data.Profile;
import it.pingflood.winted.profileservice.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitDb implements CommandLineRunner {
  private final ProfileRepository profileRepository;
  
  public void run(String... args) throws Exception {
    if (profileRepository.findAllByUsername("winted").isEmpty()) {
      profileRepository.save(Profile.builder().username("winted").build());
    }
  }
  
}
