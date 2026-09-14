package com.example.demo.model.authentication;

import com.example.demo.dto.UsersDto;
import java.time.Instant;

public class AuthenticationResponse {
  private final String accessToken;
  private final String refreshToken;
  private final Instant expirationAt;
  private final UsersDto user;

  public AuthenticationResponse(
      String accessToken, String refreshToken, Instant expirationAt, UsersDto user) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expirationAt = expirationAt;
    this.user = user;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public Instant getExpirationAt() {
    return expirationAt;
  }

  public UsersDto getUser() {
    return user;
  }

  public String getRefreshToken() {
    return refreshToken;
  }
}
