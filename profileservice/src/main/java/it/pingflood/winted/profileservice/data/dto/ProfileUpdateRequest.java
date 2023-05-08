package it.pingflood.winted.profileservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfileUpdateRequest {
  private String id;
  private String username;
  private String providerIdentityId;
}
