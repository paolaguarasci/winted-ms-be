package it.pingflood.winted.profileservice.controller;

import it.pingflood.winted.profileservice.data.dto.ProductPreferred;
import it.pingflood.winted.profileservice.data.dto.ProfileCreateRequest;
import it.pingflood.winted.profileservice.data.dto.ProfileResponse;
import it.pingflood.winted.profileservice.data.dto.ProfileUpdateRequest;
import it.pingflood.winted.profileservice.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/profile")
@Slf4j
@RequiredArgsConstructor
public class ProfileController {
  private final ProfileService profileService;
  
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  ResponseEntity<List<ProfileResponse>> handleGetAll(Principal principal) {
    log.info("Richiesta fatta dall'utente {}", principal.getName());
    return ResponseEntity.ok(profileService.getAll());
  }
  
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  ResponseEntity<ProfileResponse> handleGetOneInt(@PathVariable("id") String id, Principal principal) {
    return ResponseEntity.ok(profileService.getOneByExtId(principal.getName()));
  }
  
  @GetMapping("/my")
  @ResponseStatus(HttpStatus.OK)
  ResponseEntity<ProfileResponse> handleGetMY(@AuthenticationPrincipal Jwt token) {
    Map<String, String> map = new Hashtable<String, String>();
    map.put("user_name", token.getClaimAsString("preferred_username"));
    map.put("email", token.getClaimAsString("email"));
    map.put("name", token.getClaimAsString("name"));
    map.put("external_id", token.getClaimAsString("sub"));
    log.info("Logged user data {}", map);
    return ResponseEntity.ok(profileService.getMyByExtId(map));
  }
  
  @GetMapping("/local/{id}")
  @ResponseStatus(HttpStatus.OK)
  ResponseEntity<ProfileResponse> handleGetOneExt(@PathVariable("id") String id, Principal principal) {
    log.info("Get local profile id {}", principal.getName());
    return ResponseEntity.ok(profileService.getOneById(principal.getName()));
  }
  
  @GetMapping("/username/{username}")
  @ResponseStatus(HttpStatus.OK)
  ResponseEntity<ProfileResponse> handleGetOneByUsername(@PathVariable("username") String username, Principal principal) {
    log.info("Get profile by username {} and principal {}", username, principal.getName());
    return ResponseEntity.ok(profileService.getOneByUsername(username));
  }
  
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  ResponseEntity<ProfileResponse> handlePost(@RequestBody ProfileCreateRequest profileCreateRequest, Principal principal) {
    log.info("Registration profile by username {} and principal {}", profileCreateRequest, principal.getName());
    profileCreateRequest.setProviderIdentityId(principal.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(profileService.createOne(profileCreateRequest));
  }
  
  
  @PutMapping("{id}")
  @ResponseStatus(HttpStatus.OK)
  ResponseEntity<ProfileResponse> handlePutOne(@PathVariable("id") String id, @RequestBody ProfileUpdateRequest profileUpdateRequest, Principal principal, Authentication auth) {
    return ResponseEntity.status(HttpStatus.OK).body(profileService.updateOne(id, profileUpdateRequest));
  }
  
  @DeleteMapping("{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  ResponseEntity<Void> handleDeleteOne(@PathVariable("id") String id, Principal principal, Authentication auth) {
    profileService.deleteOne(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }


//  @GetMapping("{user_id}/preferred")
//  @ResponseStatus(HttpStatus.OK)
//  ResponseEntity<ProfileResponse> handleGetPreferred(@PathVariable("user_id") String userId, @RequestBody ProfileUpdateRequest profileUpdateRequest) {
//    return ResponseEntity.status(HttpStatus.OK).body(profileService.getPreferred(userId));
//  }
  
  
  // Chiudere il metodo! Permessi solo all'utente loggato
  @PostMapping("preferred")
  @ResponseStatus(HttpStatus.CREATED)
  ResponseEntity<Void> handleAddPreferred(@RequestBody ProductPreferred productPreferred, Principal principal, Authentication auth) {
    profileService.addPreferred(productPreferred);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
  
  @GetMapping("preferred")
  @ResponseStatus(HttpStatus.OK)
  ResponseEntity<List<String>> handleGetPreferred(Principal principal, Authentication auth) {
    return ResponseEntity.status(HttpStatus.OK).body(profileService.getPreferred());
  }
  
  
  @DeleteMapping("preferred/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  ResponseEntity<Void> handleDeleteFromPreferred(@PathVariable("id") String id, Principal principal, Authentication auth) {
    profileService.removePreferred(ProductPreferred.builder().product(id).build());
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
  
  
}
