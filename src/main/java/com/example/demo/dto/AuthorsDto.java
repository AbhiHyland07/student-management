package com.example.demo.dto;

import java.time.Instant;
import java.util.List;

public class AuthorsDto {
  private String id;
  private String displayName;
  private String slug;
  private String avatarUrl;
  private BioDto bio;
  private String title;
  private List<SocialLinkDto> socialLinks;
  private Instant createdAt;
  private Instant updatedAt;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  public BioDto getBio() {
    return bio;
  }

  public void setBio(BioDto bio) {
    this.bio = bio;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<SocialLinkDto> getSocialLinks() {
    return socialLinks;
  }

  public void setSocialLinks(List<SocialLinkDto> socialLinks) {
    this.socialLinks = socialLinks;
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

  public static class BioDto {
    private String en;
    private String sn;

    public String getEn() {
      return en;
    }

    public void setEn(String en) {
      this.en = en;
    }

    public String getSn() {
      return sn;
    }

    public void setSn(String sn) {
      this.sn = sn;
    }
  }

  public static class SocialLinkDto {
    private String platform;
    private String url;

    public String getPlatform() {
      return platform;
    }

    public void setPlatform(String platform) {
      this.platform = platform;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }
  }
}
