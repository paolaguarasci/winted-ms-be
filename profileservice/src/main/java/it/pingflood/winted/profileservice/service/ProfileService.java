package it.pingflood.winted.profileservice.service;

import it.pingflood.winted.profileservice.data.dto.*;

import java.util.List;

public interface ProfileService {
  List<ProfileResponse> getAll();
  
  ProfileResponse getOneById(String id);
  
  ProfileResponse getOneByUsername(String username);
  
  ProfileResponse createOne(ProfileCreateRequest profileCreateRequest);
  
  ProfileResponse updateOne(String id, ProfileUpdateRequest profileUpdateRequest);
  
  ProfileResponse addPreferred(ProductPreferred product);
  
  ProfileResponse removePreferred(ProductPreferred product);
  
  ProfileResponse addWardrobe(ProductWardrobe product);
  
  ProfileResponse removeWardrobe(ProductWardrobe product);
  
  ProfileResponse addDraft(ProductDraft product);
  
  ProfileResponse removeDraft(ProductDraft product);
  
  List<String> getPreferred();
  
  void deleteOne(String id);
}
