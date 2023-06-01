package it.pingflood.winted.addressservice.data;

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
@Table(name = "WINTED_ADDRESS")
public class Address extends Auditable<String> implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  private UUID id;
  private String user;
  private String nome;
  private String cognome;
  private String via;
  private String citta;
  private String numeroCivico;
  private String cap;
}
