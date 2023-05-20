package it.pingflood.winted.addressservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddressResponse implements Serializable {
  private UUID id;
  private String username;
  private String nome;
  private String cognome;
  private String via;
  private String citta;
  private String numeroCivico;
  private String cap;
}
