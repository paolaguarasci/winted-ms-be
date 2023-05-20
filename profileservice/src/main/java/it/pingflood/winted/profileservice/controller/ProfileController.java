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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profile")
@Slf4j
@RequiredArgsConstructor
public class ProfileController {
  private final ProfileService profileService;
  
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  ResponseEntity<List<ProfileResponse>> handleGetAll() {
    return ResponseEntity.ok(profileService.getAll());
  }
  
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  ResponseEntity<ProfileResponse> handleGetOneAll(@PathVariable("id") String id) {
    return ResponseEntity.ok(profileService.getOneById(id));
  }
  
  @GetMapping("/username/{username}")
  @ResponseStatus(HttpStatus.OK)
  ResponseEntity<ProfileResponse> handleGetOneByUsername(@PathVariable("username") String username) {
    return ResponseEntity.ok(profileService.getOneByUsername(username));
  }
  
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  ResponseEntity<ProfileResponse> handlePost(@RequestBody ProfileCreateRequest profileCreateRequest) {
    return ResponseEntity.status(HttpStatus.CREATED).body(profileService.createOne(profileCreateRequest));
  }
  
  
  @PutMapping("{id}")
  @ResponseStatus(HttpStatus.OK)
  ResponseEntity<ProfileResponse> handlePutOne(@PathVariable("id") String id, @RequestBody ProfileUpdateRequest profileUpdateRequest) {
    return ResponseEntity.status(HttpStatus.OK).body(profileService.updateOne(id, profileUpdateRequest));
  }
  
  @DeleteMapping("{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  ResponseEntity<Void> handleDeleteOne(@PathVariable("id") String id) {
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
  ResponseEntity<Void> handleAddPreferred(@RequestBody ProductPreferred productPreferred) {
    profileService.addPreferred(productPreferred);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
  
  @GetMapping("preferred")
  @ResponseStatus(HttpStatus.OK)
  ResponseEntity<List<String>> handleGetPreferred() {
    return ResponseEntity.status(HttpStatus.OK).body(profileService.getPreferred());
  }
  
  
  @DeleteMapping("preferred/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  ResponseEntity<Void> handleDeleteFromPreferred(@PathVariable("id") String id) {
    profileService.removePreferred(ProductPreferred.builder().product(id).build());
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
  
  
}
