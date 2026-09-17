package com.example.demo.dto;

import com.example.demo.model.enums.Permission;
import com.example.demo.model.enums.PreferredLanguage;
import com.example.demo.model.enums.Role;
import com.example.demo.model.enums.Status;
import java.util.List;

public class UpdateUserRequestDto {
  private String fullName;
  private String avatarUrl;
  private Role role;
  private Status status;
  private PreferredLanguage preferredLanguage;
  private List<Permission> permissions;

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
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
}
