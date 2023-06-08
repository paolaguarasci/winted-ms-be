package it.pingflood.winted.paymentservice.data;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "WINTED_TRANSACTION")
public class Transaction extends Auditable<String> implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  private UUID id;
  private String paymentMethodId;
  private String paymentFrom;
  private String paymentTo;
  private Double paymentImport;
  
  @Enumerated(EnumType.STRING)
  private TransactionStatus transactionStatus;
  
  @Version
  @Column(name = "optlock", columnDefinition = "integer DEFAULT 0", nullable = false)
  private long version = 0L;
}
