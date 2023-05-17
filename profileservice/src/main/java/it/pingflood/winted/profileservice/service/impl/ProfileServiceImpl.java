package it.pingflood.winted.profileservice.service.impl;

import it.pingflood.winted.profileservice.data.Profile;
import it.pingflood.winted.profileservice.data.dto.ProfileCreateRequest;
import it.pingflood.winted.profileservice.data.dto.ProfileResponse;
import it.pingflood.winted.profileservice.data.dto.ProfileUpdateRequest;
import it.pingflood.winted.profileservice.event.ProductEvent;
import it.pingflood.winted.profileservice.repository.ProfileRepository;
import it.pingflood.winted.profileservice.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProfileServiceImpl implements ProfileService {
  private final ProfileRepository profileRepository;
  private final ModelMapper modelMapper = new ModelMapper();
  private final KafkaTemplate<String, ProductEvent> productEventKafkaTemplate;
  
  @Override
  public List<ProfileResponse> getAll() {
    return profileRepository.findAll().stream().map(profile -> modelMapper.map(profile, ProfileResponse.class)).collect(Collectors.toList());
  }
  
  @Override
  public ProfileResponse getOne(String id) {
    return modelMapper.map(profileRepository.findById(id).orElseThrow(), ProfileResponse.class);
  }
  
  @Override
  public ProfileResponse createOne(ProfileCreateRequest profileCreateRequest) {
    return modelMapper.map(profileRepository.save(modelMapper.map(profileCreateRequest, Profile.class)), ProfileResponse.class);
  }
  
  @Override
  public ProfileResponse updateOne(String id, ProfileUpdateRequest profileUpdateRequest) {
    return modelMapper.map(profileRepository.save(modelMapper.map(profileUpdateRequest, Profile.class)), ProfileResponse.class);
  }
  
  @Override
  public ProfileResponse addPreferred(String productId) {
    String loggedUsername = "p1"; // TODO Procurarselo in modo consono
    productEventKafkaTemplate.send("AddToPreferred", "profile-service", new ProductEvent(productId));
    Profile loggedUser = profileRepository.findAllByUsername(loggedUsername).orElseThrow();
    loggedUser.addPreferred(productId);
    profileRepository.save(loggedUser);
    return modelMapper.map(profileRepository.save(modelMapper.map(loggedUser, Profile.class)), ProfileResponse.class);
  }
  
  @Override
  public ProfileResponse removePreferred(String productId) {
    String loggedUsername = "p1"; // TODO Procurarselo in modo consono
    Profile loggedUser = profileRepository.findAllByUsername(loggedUsername).orElseThrow();
    loggedUser.removePreferred(productId);
    productEventKafkaTemplate.send("RemoteToPreferred", "profile-service", new ProductEvent(productId));
    profileRepository.save(loggedUser);
    return modelMapper.map(profileRepository.save(modelMapper.map(loggedUser, Profile.class)), ProfileResponse.class);
  }
  
  @Override
  public ProfileResponse addWardrobe(String productId) {
    String loggedUsername = "p1"; // TODO Procurarselo in modo consono
    Profile loggedUser = profileRepository.findAllByUsername(loggedUsername).orElseThrow();
    loggedUser.addWardrobe(productId);
    profileRepository.save(loggedUser);
    return modelMapper.map(profileRepository.save(modelMapper.map(loggedUser, Profile.class)), ProfileResponse.class);
  }
  
  @Override
  public ProfileResponse removeWardrobe(String productId) {
    String loggedUsername = "p1"; // TODO Procurarselo in modo consono
    Profile loggedUser = profileRepository.findAllByUsername(loggedUsername).orElseThrow();
    loggedUser.removeWardrobe(productId);
    profileRepository.save(loggedUser);
    return modelMapper.map(profileRepository.save(modelMapper.map(loggedUser, Profile.class)), ProfileResponse.class);
  }
  
  @Override
  public ProfileResponse addDraft(String productId) {
    String loggedUsername = "p1"; // TODO Procurarselo in modo consono
    Profile loggedUser = profileRepository.findAllByUsername(loggedUsername).orElseThrow();
    loggedUser.addDraft(productId);
    profileRepository.save(loggedUser);
    return modelMapper.map(profileRepository.save(modelMapper.map(loggedUser, Profile.class)), ProfileResponse.class);
  }
  
  @Override
  public ProfileResponse removeDraft(String productId) {
    String loggedUsername = "p1"; // TODO Procurarselo in modo consono
    Profile loggedUser = profileRepository.findAllByUsername(loggedUsername).orElseThrow();
    loggedUser.removeDraft(productId);
    profileRepository.save(loggedUser);
    return modelMapper.map(profileRepository.save(modelMapper.map(loggedUser, Profile.class)), ProfileResponse.class);
  }
  
  @Override
  public void deleteOne(String id) {
    profileRepository.deleteById(id);
  }
}
