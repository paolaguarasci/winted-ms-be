package it.pingflood.winted.profileservice.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Builder
@AllArgsConstructor
@Document(value = "profile")
@Data
public class Profile {
  @Id
  private String id;
  @Indexed(unique = true)
  private String username;
  private String avatar;
  private Set<String> preferred;
  private Set<String> wardrobe;
  private Set<String> draft;
  private Double reputation;
  private String providerIdentityId;
  
  public void addPreferred(String productid) {
    preferred.add(productid);
  }
  
  public void removePreferred(String productid) {
    preferred.remove(productid);
  }
  
  public void addWardrobe(String productid) {
    wardrobe.add(productid);
  }
  
  public void removeWardrobe(String productid) {
    wardrobe.remove(productid);
  }
  
  public void addDraft(String productid) {
    draft.add(productid);
  }
  
  public void removeDraft(String productid) {
    draft.remove(productid);
  }
  
}
