package com.example.exercise.rest.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;


@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -9097952149583085191L;

  private String message = "Resource not found";

  public ResourceNotFoundException() {}
  
  public ResourceNotFoundException(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {

    return message;
  }

}
