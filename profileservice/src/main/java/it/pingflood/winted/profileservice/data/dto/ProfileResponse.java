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
}
