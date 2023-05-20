package it.pingflood.winted.profileservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfileResponse {
  private String id;
  private String username;
  private String avatar;
  private Double reputation;
  private List<String> wardrobe = new ArrayList<>();
  
  
  private String position;
  private String lastVisit;
  
  private Integer follower;
  private Integer seguiti;
  
  private Boolean googleVerified;
  private Boolean emailVerified;
  private Boolean facebookVerified;
  
}
