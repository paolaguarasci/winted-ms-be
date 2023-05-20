package it.pingflood.winted.profileservice.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

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
  private Set<String> preferred = new TreeSet<>();
  private Set<String> wardrobe = new HashSet<>();
  private Set<String> draft = new HashSet<>();
  private Double reputation;
  private String providerIdentityId;
  
  private String position;
  private String lastVisit;
  
  private Integer follower = 0;
  private Integer seguiti = 0;
  
  private Boolean googleVerified = false;
  private Boolean emailVerified = false;
  private Boolean facebookVerified = false;
  
  public void addPreferred(String productid) {
    if (this.preferred == null) {
      this.preferred = new TreeSet<>();
    }
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
  
  
  public void setGoogleVerified() {
    this.googleVerified = true;
  }
  
  public void setEmailVerified() {
    this.emailVerified = true;
  }
  
  public void setFacebookVerified() {
    this.facebookVerified = true;
  }
}
