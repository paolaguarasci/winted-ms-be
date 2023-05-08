package it.pingflood.winted.profileservice.controller;

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
    return ResponseEntity.ok(profileService.getOne(id));
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
}
