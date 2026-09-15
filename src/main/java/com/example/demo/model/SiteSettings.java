package com.example.demo.model;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "siteSettings")
public class SiteSettings {
  @Id private String id;

  private SiteName siteName;
  private String contactEmail;
  private String defaultShareImageUrl;
  private Instant updatedAt;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public SiteName getSiteName() {
    return siteName;
  }

  public void setSiteName(SiteName siteName) {
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

  public static class SiteName {
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
