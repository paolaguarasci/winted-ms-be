package it.pingflood.winted.profileservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfileOwnerResponse {
  private String id;
  private String username;
  private List<String> preferred;
  private List<String> wardrobe;
  private List<String> draft;
  private Double reputation;
  private String providerIdentityId;
}
