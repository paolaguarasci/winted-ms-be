package it.pingflood.winted.resourceservice.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
@Data
public class AWSConfig {
  @Value("${app.aws.s3.bouquetName}")
  public String awsBouquet;
  @Value("${app.aws.credential.accessKey}")
  public String accessKey;
  @Value("${app.aws.credential.secretKey}")
  public String secretKey;
}
