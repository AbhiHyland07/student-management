package com.example.demo.dto;

import java.time.Instant;

public class SiteSettingsDto {
  private String id;
  private SiteNameDto siteName;
  private String contactEmail;
  private String defaultShareImageUrl;
  private Instant updatedAt;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public SiteNameDto getSiteName() {
    return siteName;
  }

  public void setSiteName(SiteNameDto siteName) {
    this.siteName = siteName;
  }

  public String getContactEmail() {
    return contactEmail;
  }

  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
  }

  public String getDefaultShareImageUrl() {
    return defaultShareImageUrl;
  }

  public void setDefaultShareImageUrl(String defaultShareImageUrl) {
    this.defaultShareImageUrl = defaultShareImageUrl;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  public static class SiteNameDto {
    private String english;
    private String bengali;

    public String getEnglish() {
      return english;
    }

    public void setEnglish(String english) {
      this.english = english;
    }

    public String getBengali() {
      return bengali;
    }

    public void setBengali(String bengali) {
      this.bengali = bengali;
    }
  }
}
