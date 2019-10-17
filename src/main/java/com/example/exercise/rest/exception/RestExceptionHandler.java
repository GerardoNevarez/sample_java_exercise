package com.example.exercise.rest.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.Data;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
  
  @Data
  private class Message {
    
    private final String message;
    
    public Message(String message) {
      this.message = message;
    }
  }

  @ExceptionHandler(value = ResourceNotFoundException.class)
  protected ResponseEntity<Object> handle(ResourceNotFoundException ex, WebRequest request) {
    return handleExceptionInternal(ex, new Message(ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(value = {ResourceModificationErrorException.class, RuntimeException.class})
  protected ResponseEntity<Object> handle(ResourceModificationErrorException ex, WebRequest request) {
    return handleExceptionInternal(ex, new Message(ex.getMessage()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }

}
