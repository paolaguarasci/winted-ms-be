package it.pingflood.winted.profileservice.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@AllArgsConstructor
@Document(value = "product")
@Data
public class Profile {
  @Id
  private String id;
  @Indexed(unique = true)
  private String username;
  private String providerIdentityId;
}
