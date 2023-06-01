package it.pingflood.winted.addressservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddressPUTRequest implements Serializable {
  private String id;
  private String user;
  private String nome;
  private String cognome;
  private String via;
  private String citta;
  private String numeroCivico;
  private String cap;
}
