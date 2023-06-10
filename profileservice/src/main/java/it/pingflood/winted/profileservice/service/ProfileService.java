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
  
  ProfileResponse addPreferred(ProductPreferred product, String principal);
  
  ProfileResponse removePreferred(ProductPreferred product, String principal);
  
  ProfileResponse addWardrobe(ProductWardrobe product, String principal);
  
  ProfileResponse removeWardrobe(ProductWardrobe product, String principal);
  
  ProfileResponse addDraft(ProductDraft product, String principal);
  
  ProfileResponse removeDraft(ProductDraft product, String principal);
  
  List<String> getPreferred(String principal);
  
  void deleteOne(String id);
}
