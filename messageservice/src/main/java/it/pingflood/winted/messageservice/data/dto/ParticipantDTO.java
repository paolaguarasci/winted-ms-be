package it.pingflood.winted.messageservice.data.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class ParticipantDTO {
  private String username;
}
