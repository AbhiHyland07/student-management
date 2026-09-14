package com.example.demo.model.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ForgotPasswordRequest {
  @Email(message = "Email should be valid")
  @Pattern(
      regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$",
      message = "Email must be lowercase and in valid format (e.g., user@gmail.com)")
  @NotBlank(message = "Email is required")
  private String email;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
