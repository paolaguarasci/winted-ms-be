package it.pingflood.winted.messageservice.event;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericEvent {
  private String payload;
}
