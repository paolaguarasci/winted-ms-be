package it.pingflood.winted.messageservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewReplayEvent {
  private String conversationId;
  private String actor1Id;
  private String actor2Id;
}
