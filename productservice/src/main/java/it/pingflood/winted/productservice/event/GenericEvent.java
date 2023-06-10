package it.pingflood.winted.productservice.event;

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
