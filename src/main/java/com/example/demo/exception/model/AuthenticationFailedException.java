package com.example.demo.exception.model;

public class AuthenticationFailedException extends RuntimeException {
  public AuthenticationFailedException(String message) {
    super(message);
  }
}
