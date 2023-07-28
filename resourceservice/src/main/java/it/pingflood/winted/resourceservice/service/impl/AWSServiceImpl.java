package it.pingflood.winted.resourceservice.service.impl;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import it.pingflood.winted.resourceservice.config.AWSConfig;
import it.pingflood.winted.resourceservice.service.AWSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;

@Service
@Slf4j
//@PropertySource("classpath:application-dev.yml")
public class AWSServiceImpl implements AWSService {
  
  private final AmazonS3 s3Client;
  private final AWSConfig awsConfig;
  
  public AWSServiceImpl(AWSConfig awsConfig) {
    this.awsConfig = awsConfig;
    AWSCredentials credentials = new BasicAWSCredentials(this.awsConfig.getAccessKey(), this.awsConfig.getSecretKey());
    this.s3Client = AmazonS3ClientBuilder
      .standard()
      .withCredentials(new AWSStaticCredentialsProvider(credentials))
      .withRegion(Regions.EU_WEST_1)
      .build();
  }
  
  @Override
  public String saveTxt(byte[] file) {
    String objectKey = UUID.randomUUID() + ".txt";
    s3Client.putObject(this.awsConfig.getAwsBouquet(), objectKey, Arrays.toString(file));
    return objectKey;
  }
  
  @Override
  public String saveObj(byte[] file, String fileExtension) {
    InputStream inputStream = new ByteArrayInputStream(file);
    ObjectMetadata objectMetadata = new ObjectMetadata();
    String objectKey = UUID.randomUUID() + "." + fileExtension;
    s3Client.putObject(new PutObjectRequest(this.awsConfig.getAwsBouquet(), objectKey, inputStream, objectMetadata));
    return objectKey;
  }
  
  @Override
  public S3Object getObj(String key) {
    return s3Client.getObject(new GetObjectRequest(this.awsConfig.getAwsBouquet(), key));
  }
  
  @Override
  public String getBaseUrl() {
    return s3Client.getUrl(this.awsConfig.getAwsBouquet(), "/").toExternalForm();
  }
  
  @Override
  public String getObjectUrl(String key) {
    return s3Client.getUrl(this.awsConfig.getAwsBouquet(), key).toExternalForm();
  }
  
}
