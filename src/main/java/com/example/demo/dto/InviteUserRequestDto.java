package com.example.demo.dto;

import com.example.demo.model.enums.Permission;
import com.example.demo.model.enums.PreferredLanguage;
import com.example.demo.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.List;

public class InviteUserRequestDto {
  @NotBlank(message = "Full name is required")
  private String fullName;

  @Email(message = "Email should be valid")
  @Pattern(
      regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$",
      message = "Email must be lowercase and in valid format (e.g., user@gmail.com)")
  @NotBlank(message = "Email is required")
  private String email;

  private Role role;
  private String avatarUrl;
  private PreferredLanguage preferredLanguage;
  private List<Permission> permissions;
  private String authorId;

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  public PreferredLanguage getPreferredLanguage() {
    return preferredLanguage;
  }

  public void setPreferredLanguage(PreferredLanguage preferredLanguage) {
    this.preferredLanguage = preferredLanguage;
  }

  public List<Permission> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<Permission> permissions) {
    this.permissions = permissions;
  }

  public String getAuthorId() {
    return authorId;
  }

  public void setAuthorId(String authorId) {
    this.authorId = authorId;
  }
}
