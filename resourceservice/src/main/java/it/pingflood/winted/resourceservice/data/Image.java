package it.pingflood.winted.resourceservice.data;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.util.Objects;

@DiscriminatorValue("image")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Image extends Resource {
  
  
  @Enumerated(EnumType.STRING)
  FEATURED isFeatured = FEATURED.FALSE;
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Image image = (Image) o;
    return getId() != null && Objects.equals(getId(), image.getId());
  }
  
  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
  
  public enum FEATURED {
    TRUE, FALSE
  }
}
