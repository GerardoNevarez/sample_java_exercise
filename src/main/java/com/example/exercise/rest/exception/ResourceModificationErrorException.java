package com.example.exercise.rest.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;


@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ResourceModificationErrorException extends RuntimeException {

  private static final long serialVersionUID = -7605662388991161278L;
  private String message = "Unable to modify Resource";

  public ResourceModificationErrorException() {}

    public ResourceModificationErrorException(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {

    return message;
  }

}
