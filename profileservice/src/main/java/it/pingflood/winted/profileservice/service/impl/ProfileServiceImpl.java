package it.pingflood.winted.profileservice.service.impl;

import it.pingflood.winted.profileservice.data.Profile;
import it.pingflood.winted.profileservice.data.dto.ProfileCreateRequest;
import it.pingflood.winted.profileservice.data.dto.ProfileResponse;
import it.pingflood.winted.profileservice.data.dto.ProfileUpdateRequest;
import it.pingflood.winted.profileservice.repository.ProfileRepository;
import it.pingflood.winted.profileservice.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileServiceImpl implements ProfileService {
  private final ProfileRepository profileRepository;
  private final ModelMapper modelMapper = new ModelMapper();
  
  
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
  public void deleteOne(String id) {
    profileRepository.deleteById(id);
  }
}
