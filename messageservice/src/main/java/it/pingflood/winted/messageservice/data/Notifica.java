package it.pingflood.winted.messageservice.data;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "notifica")
public class Notifica {
  private String id;
  private String user;
  private LocalDateTime timestamp;
  private String content;
  private String timeAgo;
  private String prodottoCorrelato;
  private Boolean read;
}
