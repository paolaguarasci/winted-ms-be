package it.pingflood.winted.resourceservice.controller;

import it.pingflood.winted.resourceservice.data.dto.ImageRequest;
import it.pingflood.winted.resourceservice.data.dto.ImageResponse;
import it.pingflood.winted.resourceservice.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/resource")
@Slf4j
public class ResourceController {
  
  private final ResourceService resourceService;
  
  public ResourceController(ResourceService resourceService) {
    this.resourceService = resourceService;
  }
  
  
  @GetMapping("/image/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ImageResponse> getImage(@PathVariable("id") UUID id) {
    return ResponseEntity.ok(resourceService.getOne(id));
  }
  
  @PostMapping(value = "/image", headers = ("content-type=multipart/form-data"))
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ImageResponse> saveImage(ImageRequest imageRequest) {
    
    log.debug("UPLOAD IMAGE Type {}", imageRequest.getFile().getContentType());
    log.debug("UPLOAD IMAGE Size {}", imageRequest.getFile().getSize());
    log.debug("UPLOAD IMAGE Original File Name {}", imageRequest.getFile().getOriginalFilename());
    
    return ResponseEntity.ok(resourceService.saveOne(imageRequest));
  }
}
