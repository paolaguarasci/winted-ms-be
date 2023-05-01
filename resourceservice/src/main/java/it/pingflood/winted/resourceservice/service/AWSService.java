package it.pingflood.winted.resourceservice.service;

public interface AWSService {
  String saveObj(byte[] file);
  
  String saveTxt(byte[] file);
  
  void getObj();
  
  String getBaseUrl();
  
  String getObjectUrl(String key);
}
