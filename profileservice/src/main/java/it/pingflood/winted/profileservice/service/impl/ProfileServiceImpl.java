package it.pingflood.winted.profileservice.service.impl;

import com.querydsl.core.types.Predicate;
import it.pingflood.winted.profileservice.data.Profile;
import it.pingflood.winted.profileservice.data.QProfile;
import it.pingflood.winted.profileservice.data.dto.*;
import it.pingflood.winted.profileservice.event.ProductEvent;
import it.pingflood.winted.profileservice.repository.ProfileRepository;
import it.pingflood.winted.profileservice.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class ProfileServiceImpl implements ProfileService {
  private final ProfileRepository profileRepository;
  private final ModelMapper modelMapper;
  private final KafkaTemplate<String, ProductEvent> productEventKafkaTemplate;
  
  public ProfileServiceImpl(ProfileRepository profileRepository, KafkaTemplate<String, ProductEvent> productEventKafkaTemplate) {
    this.profileRepository = profileRepository;
    this.productEventKafkaTemplate = productEventKafkaTemplate;
    modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
      .setFieldMatchingEnabled(true)
      .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
  }
  
  @Override
  public List<ProfileResponse> getAll() {
    return profileRepository.findAll().stream().map(profile -> modelMapper.map(profile, ProfileResponse.class)).collect(Collectors.toList());
  }
  
  @Override
  public List<ProfileResponse> search(String query) {
    QProfile qProfile = new QProfile("profile");
    Predicate querySearch = qProfile.username.containsIgnoreCase(query).or(qProfile.firstName.containsIgnoreCase(query)).or(qProfile.lastName.containsIgnoreCase(query));
    List<Profile> profiles = (List<Profile>) profileRepository.findAll(querySearch);
    log.info("Trovati {} profili con i criteri di ricerca selezionati", profiles.size());
    return profiles.stream().map(profile -> modelMapper.map(profile, ProfileResponse.class)).collect(Collectors.toList());
  }
  
  @Override
  public ProfileResponse getOneById(String id) {
    log.info("Profilo {}", id);
    return modelMapper.map(profileRepository.findById(id).orElseThrow(), ProfileResponse.class);
  }
  
  @Override
  public ProfileResponse getOneByExtId(String loggedExternalUserid) {
    return modelMapper.map(profileRepository.findByProviderIdentityId(loggedExternalUserid).orElseThrow(), ProfileResponse.class);
  }
  
  @Override
  public ProfileResponse getMyByExtId(Map<String, String> userLoggedData) {
    
    String username = userLoggedData.get("user_name");
    String externalId = userLoggedData.get("external_id");
    
    if (profileRepository.findByProviderIdentityId(externalId).isEmpty()) {
      log.info("Utente non trovato, lo sto creando - {}", externalId);
      return createOne(ProfileCreateRequest.builder().username(username).providerIdentityId(externalId).build());
    }
    return modelMapper.map(profileRepository.findByProviderIdentityId(externalId).orElseThrow(), ProfileResponse.class);
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
    
    log.info("Creazione utente {} {}", profileCreateRequest.getUsername(), profileCreateRequest.getProviderIdentityId());
    
    Profile newProfile = modelMapper.map(profileCreateRequest, Profile.class);
    newProfile.setAvatar("https://api.dicebear.com/6.x/initials/svg?seed=" + profileCreateRequest.getUsername());
    Profile savedProfile = profileRepository.save(newProfile);
    ProfileResponse profileResponse = modelMapper.map(savedProfile, ProfileResponse.class);
    log.info("Profilo creato!");
    return profileResponse;
  }
  
  @Override
  public ProfileResponse updateOne(String id, ProfileUpdateRequest profileUpdateRequest) {
    return modelMapper.map(profileRepository.save(modelMapper.map(profileUpdateRequest, Profile.class)), ProfileResponse.class);
  }
  
  @Override
  public ProfileResponse addPreferred(ProductPreferred product) {
    String loggedUsername = getLoggedUsername();
    productEventKafkaTemplate.send("AddToPreferred", "profile-service", new ProductEvent(product.getProduct()));
    Profile loggedUser = profileRepository.findAllByUsername(loggedUsername).orElseThrow();
    loggedUser.addPreferred(product.getProduct());
    profileRepository.save(loggedUser);
    return modelMapper.map(profileRepository.save(modelMapper.map(loggedUser, Profile.class)), ProfileResponse.class);
  }
  
  @Override
  public ProfileResponse removePreferred(ProductPreferred product) {
    String loggedUsername = getLoggedUsername();
    Profile loggedUser = profileRepository.findAllByUsername(loggedUsername).orElseThrow();
    loggedUser.removePreferred(product.getProduct());
    productEventKafkaTemplate.send("RemoteToPreferred", "profile-service", new ProductEvent(product.getProduct()));
    profileRepository.save(loggedUser);
    return modelMapper.map(profileRepository.save(modelMapper.map(loggedUser, Profile.class)), ProfileResponse.class);
  }
  
  @Override
  public ProfileResponse addWardrobe(ProductWardrobe product) {
    String loggedUsername = getLoggedUsername();
    Profile loggedUser = profileRepository.findAllByUsername(loggedUsername).orElseThrow();
    loggedUser.addWardrobe(product.getProduct());
    profileRepository.save(loggedUser);
    return modelMapper.map(profileRepository.save(modelMapper.map(loggedUser, Profile.class)), ProfileResponse.class);
  }
  
  @Override
  public ProfileResponse removeWardrobe(ProductWardrobe product) {
    String loggedUsername = getLoggedUsername();
    Profile loggedUser = profileRepository.findAllByUsername(loggedUsername).orElseThrow();
    loggedUser.removeWardrobe(product.getProduct());
    profileRepository.save(loggedUser);
    return modelMapper.map(profileRepository.save(modelMapper.map(loggedUser, Profile.class)), ProfileResponse.class);
  }
  
  @Override
  public ProfileResponse addDraft(ProductDraft product) {
    String loggedUsername = getLoggedUsername();
    Profile loggedUser = profileRepository.findAllByUsername(loggedUsername).orElseThrow();
    loggedUser.addDraft(product.getProduct());
    profileRepository.save(loggedUser);
    return modelMapper.map(profileRepository.save(modelMapper.map(loggedUser, Profile.class)), ProfileResponse.class);
  }
  
  @Override
  public ProfileResponse removeDraft(ProductDraft product) {
    String loggedUsername = getLoggedUsername();
    Profile loggedUser = profileRepository.findAllByUsername(loggedUsername).orElseThrow();
    loggedUser.removeDraft(product.getProduct());
    profileRepository.save(loggedUser);
    return modelMapper.map(profileRepository.save(modelMapper.map(loggedUser, Profile.class)), ProfileResponse.class);
  }
  
  @Override
  public List<String> getPreferred() {
    String loggedUsername = getLoggedUsername();
    Profile loggedUser = profileRepository.findAllByUsername(loggedUsername).orElseThrow();
    return loggedUser.getPreferred().stream().toList();
  }
  
  @Override
  public void deleteOne(String id) {
    profileRepository.deleteById(id);
  }
  
  private String getLoggedUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication instanceof AnonymousAuthenticationToken)) {
      String currentUserName = authentication.getName();
      Profile profile = profileRepository.findByProviderIdentityId(currentUserName).orElseThrow();
      log.info("Utente loggato - {}", profile.getUsername());
      return profile.getUsername();
    }
    return null;
  }
  
  
}
