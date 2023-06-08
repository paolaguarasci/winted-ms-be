package it.pingflood.winted.addressservice.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
@RequiredArgsConstructor
public class GenericHandler extends ResponseEntityExceptionHandler {
  
  
  private final ObjectMapper objectMapper;
  
  @SneakyThrows
  @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
  protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
    Map<String, String> errors = new HashMap<>();
    errors.put("error", "illegal arguments");
    String bodyOfResponse = objectMapper.writeValueAsString(errors);
    return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
  }
  
  
  @SneakyThrows
  @ExceptionHandler(value = {NoSuchElementException.class})
  protected ResponseEntity<Object> handleNoValue(RuntimeException ex, WebRequest request) {
    Map<String, String> errors = new HashMap<>();
    errors.put("error", "no value present");
    String bodyOfResponse = objectMapper.writeValueAsString(errors);
    return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NO_CONTENT, request);
  }
  
}
