package it.pingflood.winted.resourceservice.service;

import com.amazonaws.services.s3.model.S3Object;

public interface AWSService {
  String saveObj(byte[] file);
  
  String saveTxt(byte[] file);
  
  S3Object getObj(String key);
  
  String getBaseUrl();
  
  String getObjectUrl(String key);
}
