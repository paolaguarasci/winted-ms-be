package it.pingflood.winted.paymentservice.data.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
public class RolesDTO implements Serializable {
  private List<String> roles;
}
