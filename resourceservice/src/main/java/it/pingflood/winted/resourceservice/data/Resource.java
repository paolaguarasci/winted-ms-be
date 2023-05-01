package it.pingflood.winted.resourceservice.data;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
@DiscriminatorColumn(name = "type")
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  private UUID id;
  private String productRelated;
  
  private String fileName;
  private String fileType;
  private String fileUrl;
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Resource resource = (Resource) o;
    return getId() != null && Objects.equals(getId(), resource.getId());
  }
  
  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
