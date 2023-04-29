package it.pingflood.winted.orderservice.data;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "WINTED_ORDER")
public class Order {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  @JdbcTypeCode(SqlTypes.BIGINT)
  private Long id;
  
  private String user_id;
  private String product_id;
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Order order = (Order) o;
    return getId() != null && Objects.equals(getId(), order.getId());
  }
  
  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
