package it.pingflood.winted.messageservice.controller;

import it.pingflood.winted.messageservice.data.dto.NotificaRequest;
import it.pingflood.winted.messageservice.data.dto.NotificaResponse;
import it.pingflood.winted.messageservice.service.NotificaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/notifica")
@RequiredArgsConstructor
public class NotificaController {
  
  private final NotificaService notificaService;
  
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<List<NotificaResponse>> getAllByLoggedUser(Principal principal) {
    return ResponseEntity.ok(notificaService.getAllByLoggedUser(principal.getName()));
  }
  
  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<List<NotificaResponse>> marksAllRead(@RequestBody List<NotificaRequest> notificaRequests, Principal principal) {
    return ResponseEntity.ok(notificaService.marksAllRead(notificaRequests, principal.getName()));
  }
}
