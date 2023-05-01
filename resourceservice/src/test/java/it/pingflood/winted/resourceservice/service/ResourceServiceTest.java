package it.pingflood.winted.resourceservice.service;

import it.pingflood.winted.resourceservice.data.dto.ImageRequest;
import it.pingflood.winted.resourceservice.data.dto.ImageResponse;
import it.pingflood.winted.resourceservice.repository.ResourceRepository;
import it.pingflood.winted.resourceservice.service.impl.ResourceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ResourceServiceTest {
  
  @Mock
  private ResourceRepository resourceRepository;
  
  @InjectMocks
  private ResourceServiceImpl resourceService;
  
  private ImageRequest imageRequest;
  private ImageResponse imageResponse;
  
  @BeforeEach
  void setup() {
  }
  
  @DisplayName("Save - save correct img")
  @Test
  void test_saveOne_1() {
  
  }
  
  @DisplayName("Save - try to save bad data")
  @Test
  void test_saveOne_2() {
  
  }
}
