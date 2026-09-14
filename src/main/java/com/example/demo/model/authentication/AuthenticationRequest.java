package com.example.demo.model.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AuthenticationRequest {
  @Email(message = "Email should be valid")
  @Pattern(
      regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$",
      message = "Email must be lowercase and in valid format (e.g., user@gmail.com)")
  @NotBlank(message = "Email is required")
  private String email;

  @NotBlank(message = "Password is required")
  private String password;

  private Boolean rememberMe;

  public AuthenticationRequest(String email, String password, Boolean rememberMe) {
    this.email = email;
    this.password = password;
    this.rememberMe = rememberMe;
  }

  public AuthenticationRequest() {}

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Boolean getRememberMe() {
    return rememberMe;
  }

  public void setRememberMe(Boolean rememberMe) {
    this.rememberMe = rememberMe;
  }
}
