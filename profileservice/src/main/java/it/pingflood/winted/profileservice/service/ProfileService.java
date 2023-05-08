package it.pingflood.winted.profileservice.service;

import it.pingflood.winted.profileservice.data.dto.ProfileCreateRequest;
import it.pingflood.winted.profileservice.data.dto.ProfileResponse;
import it.pingflood.winted.profileservice.data.dto.ProfileUpdateRequest;

import java.util.List;

public interface ProfileService {
  List<ProfileResponse> getAll();
  
  ProfileResponse getOne(String id);
  
  ProfileResponse createOne(ProfileCreateRequest profileCreateRequest);
  
  ProfileResponse updateOne(String id, ProfileUpdateRequest profileUpdateRequest);
  
  void deleteOne(String id);
}
