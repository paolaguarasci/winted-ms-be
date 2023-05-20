package it.pingflood.winted.profileservice.service.impl;

import it.pingflood.winted.profileservice.data.Profile;
import it.pingflood.winted.profileservice.data.dto.*;
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
  public ProfileResponse getOneById(String id) {
    return modelMapper.map(profileRepository.findById(id).orElseThrow(), ProfileResponse.class);
  }
  
  @Override
  public ProfileResponse getOneByUsername(String username) {
    if (profileRepository.findByUsername(username).isEmpty()) {
      profileRepository.save(Profile.builder().username(username).build());
    }
    return modelMapper.map(profileRepository.findByUsername(username), ProfileResponse.class);
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
  public ProfileResponse addPreferred(ProductPreferred product) {
    String loggedUsername = "paola"; // TODO Procurarselo in modo consono
    productEventKafkaTemplate.send("AddToPreferred", "profile-service", new ProductEvent(product.getProduct()));
    Profile loggedUser = profileRepository.findAllByUsername(loggedUsername).orElseThrow();
    loggedUser.addPreferred(product.getProduct());
    profileRepository.save(loggedUser);
    return modelMapper.map(profileRepository.save(modelMapper.map(loggedUser, Profile.class)), ProfileResponse.class);
  }
  
  @Override
  public ProfileResponse removePreferred(ProductPreferred product) {
    String loggedUsername = "paola"; // TODO Procurarselo in modo consono
    Profile loggedUser = profileRepository.findAllByUsername(loggedUsername).orElseThrow();
    loggedUser.removePreferred(product.getProduct());
    productEventKafkaTemplate.send("RemoteToPreferred", "profile-service", new ProductEvent(product.getProduct()));
    profileRepository.save(loggedUser);
    return modelMapper.map(profileRepository.save(modelMapper.map(loggedUser, Profile.class)), ProfileResponse.class);
  }
  
  @Override
  public ProfileResponse addWardrobe(ProductWardrobe product) {
    String loggedUsername = "paola"; // TODO Procurarselo in modo consono
    Profile loggedUser = profileRepository.findAllByUsername(loggedUsername).orElseThrow();
    loggedUser.addWardrobe(product.getProduct());
    profileRepository.save(loggedUser);
    return modelMapper.map(profileRepository.save(modelMapper.map(loggedUser, Profile.class)), ProfileResponse.class);
  }
  
  @Override
  public ProfileResponse removeWardrobe(ProductWardrobe product) {
    String loggedUsername = "paola"; // TODO Procurarselo in modo consono
    Profile loggedUser = profileRepository.findAllByUsername(loggedUsername).orElseThrow();
    loggedUser.removeWardrobe(product.getProduct());
    profileRepository.save(loggedUser);
    return modelMapper.map(profileRepository.save(modelMapper.map(loggedUser, Profile.class)), ProfileResponse.class);
  }
  
  @Override
  public ProfileResponse addDraft(ProductDraft product) {
    String loggedUsername = "paola"; // TODO Procurarselo in modo consono
    Profile loggedUser = profileRepository.findAllByUsername(loggedUsername).orElseThrow();
    loggedUser.addDraft(product.getProduct());
    profileRepository.save(loggedUser);
    return modelMapper.map(profileRepository.save(modelMapper.map(loggedUser, Profile.class)), ProfileResponse.class);
  }
  
  @Override
  public ProfileResponse removeDraft(ProductDraft product) {
    String loggedUsername = "paola"; // TODO Procurarselo in modo consono
    Profile loggedUser = profileRepository.findAllByUsername(loggedUsername).orElseThrow();
    loggedUser.removeDraft(product.getProduct());
    profileRepository.save(loggedUser);
    return modelMapper.map(profileRepository.save(modelMapper.map(loggedUser, Profile.class)), ProfileResponse.class);
  }
  
  @Override
  public List<String> getPreferred() {
    String loggedUsername = "paola"; // TODO Procurarselo in modo consono
    Profile loggedUser = profileRepository.findAllByUsername(loggedUsername).orElseThrow();
    return loggedUser.getPreferred().stream().toList();
  }
  
  @Override
  public void deleteOne(String id) {
    profileRepository.deleteById(id);
  }
}
