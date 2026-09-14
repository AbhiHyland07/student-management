package com.example.demo.model;

import com.example.demo.model.enums.VideoProvider;

public class Video {
  private VideoProvider provider;
  private String url;
  private String embeddedUrl;
  private String thumbnailUrl;

  public VideoProvider getProvider() {
    return provider;
  }

  public void setProvider(VideoProvider provider) {
    this.provider = provider;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getEmbeddedUrl() {
    return embeddedUrl;
  }

  public void setEmbeddedUrl(String embeddedUrl) {
    this.embeddedUrl = embeddedUrl;
  }

  public String getThumbnailUrl() {
    return thumbnailUrl;
  }

  public void setThumbnailUrl(String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
  }
}
