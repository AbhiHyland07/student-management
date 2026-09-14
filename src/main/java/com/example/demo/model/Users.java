package com.example.demo.model;

import com.example.demo.model.enums.Permission;
import com.example.demo.model.enums.PreferredLanguage;
import com.example.demo.model.enums.Role;
import com.example.demo.model.enums.Status;
import java.time.Instant;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class Users {
  @Id private String id;

  private String fullName;
  private String email;
  private String passwordHash;
  private Role role;
  private String authorId;
  private String avatarUrl;
  private Status status;
  private PreferredLanguage preferredLanguage;
  private List<Permission> permissions;
  private String passwordResetToken;
  private Instant passwordResetTokenExpiresAt;
  private Instant lastLoginAt;
  private Instant createdAt;
  private Instant updatedAt;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

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

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public String getAuthorId() {
    return authorId;
  }

  public void setAuthorId(String authorId) {
    this.authorId = authorId;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
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

  public String getPasswordResetToken() {
    return passwordResetToken;
  }

  public void setPasswordResetToken(String passwordResetToken) {
    this.passwordResetToken = passwordResetToken;
  }

  public Instant getPasswordResetTokenExpiresAt() {
    return passwordResetTokenExpiresAt;
  }

  public void setPasswordResetTokenExpiresAt(Instant passwordResetTokenExpiresAt) {
    this.passwordResetTokenExpiresAt = passwordResetTokenExpiresAt;
  }

  public Instant getLastLoginAt() {
    return lastLoginAt;
  }

  public void setLastLoginAt(Instant lastLoginAt) {
    this.lastLoginAt = lastLoginAt;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public String toString() {
    return "Users{"
        + "id='"
        + id
        + '\''
        + ", fullName='"
        + fullName
        + '\''
        + ", email='"
        + email
        + '\''
        + ", passwordHash='"
        + passwordHash
        + '\''
        + ", role="
        + role
        + ", authorId='"
        + authorId
        + '\''
        + ", avatarUrl='"
        + avatarUrl
        + '\''
        + ", status="
        + status
        + ", preferredLanguage="
        + preferredLanguage
        + ", permissions="
        + permissions
        + ", passwordResetToken='"
        + passwordResetToken
        + '\''
        + ", passwordResetTokenExpiresAt="
        + passwordResetTokenExpiresAt
        + ", lastLoginAt="
        + lastLoginAt
        + ", createdAt="
        + createdAt
        + ", updatedAt="
        + updatedAt
        + '}';
  }
}
