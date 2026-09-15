package com.example.demo.exception.model;

public class TokenInvalidException extends RuntimeException {
  public TokenInvalidException(String message) {
    super(message);
  }
}
