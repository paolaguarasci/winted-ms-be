package it.pingflood.winted.paymentservice.data;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "WINTED_PAYMENT_METHOD")
public class PaymentMethod {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  private UUID id;
  private String username;
  private String gestore;
  private String titolareCarta;
  private String numeroCarta;
  private String dataScadenza;
  private String ccv;
}
