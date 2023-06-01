package it.pingflood.winted.profileservice.service;

import it.pingflood.winted.profileservice.data.dto.*;

import java.util.List;
import java.util.Map;

public interface ProfileService {
  List<ProfileResponse> getAll();
  
  List<ProfileResponse> search(String query);
  
  ProfileResponse getOneById(String id);
  
  ProfileResponse getOneByExtId(String loggedExternalUserid);
  
  ProfileResponse getMyByExtId(Map<String, String> userLoggedData);
  
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
