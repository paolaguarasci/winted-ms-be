package it.pingflood.winted.resourceservice.controller;

import it.pingflood.winted.resourceservice.data.Resource;
import it.pingflood.winted.resourceservice.data.dto.ImageRequest;
import it.pingflood.winted.resourceservice.data.dto.ImageResponse;
import it.pingflood.winted.resourceservice.service.ResourceService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/resource")
@Slf4j
public class ResourceController {
  
  private final ResourceService resourceService;
  
  public ResourceController(ResourceService resourceService) {
    this.resourceService = resourceService;
  }
  
  @GetMapping(value = "/image/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<byte[]> getImage(@PathVariable("id") UUID id) {
    return ResponseEntity.ok(resourceService.getOne(id));
  }
  
  @PostMapping(value = "/image", headers = ("content-type=multipart/form-data"))
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<List<ImageResponse>> saveImages(ImageRequest imageRequest) {
    log.debug("RICHIESTA SAVE IMG relativa al prodotto {}", imageRequest.getProductRelated());
    return ResponseEntity.ok(resourceService.saveMultiple(imageRequest));
  }
  
  @SneakyThrows
  @GetMapping(value = "/image-unsafe")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<List<Resource>> getImageUnsafe(@RequestParam("s") String query) {
    String result = java.net.URLDecoder.decode(query, StandardCharsets.UTF_8);
    return ResponseEntity.ok(resourceService.unsafeGetOne(result));
  }
  
  @SneakyThrows
  @GetMapping(value = "/image-unsafe-jpa")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<List<Resource>> getImageUnsafeJPA(@RequestParam("s") String query) {
    String result = java.net.URLDecoder.decode(query, StandardCharsets.UTF_8);
    return ResponseEntity.ok(resourceService.unsafeGetOneWithJPA(result));
  }
}
