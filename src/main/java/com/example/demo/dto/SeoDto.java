package com.example.demo.dto;

import java.util.List;

public class SeoDto {
  private SeoDetailsDto en;
  private SeoDetailsDto bn;

  public SeoDetailsDto getEn() {
    return en;
  }

  public void setEn(SeoDetailsDto en) {
    this.en = en;
  }

  public SeoDetailsDto getBn() {
    return bn;
  }

  public void setBn(SeoDetailsDto bn) {
    this.bn = bn;
  }

  public static class SeoDetailsDto {
    private String metaTitle;
    private String metaDescription;
    private String focusKeywords;
    private String canonicalUrl;
    private String ogImageUrl;
    private Boolean robotIndex;
    private Boolean robotFollow;
    private String schemaType;
    private List<String> keyTakeaway;

    public String getMetaTitle() {
      return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
      this.metaTitle = metaTitle;
    }

    public String getMetaDescription() {
      return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
      this.metaDescription = metaDescription;
    }

    public String getFocusKeywords() {
      return focusKeywords;
    }

    public void setFocusKeywords(String focusKeywords) {
      this.focusKeywords = focusKeywords;
    }

    public String getCanonicalUrl() {
      return canonicalUrl;
    }

    public void setCanonicalUrl(String canonicalUrl) {
      this.canonicalUrl = canonicalUrl;
    }

    public String getOgImageUrl() {
      return ogImageUrl;
    }

    public void setOgImageUrl(String ogImageUrl) {
      this.ogImageUrl = ogImageUrl;
    }

    public Boolean getRobotIndex() {
      return robotIndex;
    }

    public void setRobotIndex(Boolean robotIndex) {
      this.robotIndex = robotIndex;
    }

    public Boolean getRobotFollow() {
      return robotFollow;
    }

    public void setRobotFollow(Boolean robotFollow) {
      this.robotFollow = robotFollow;
    }

    public String getSchemaType() {
      return schemaType;
    }

    public void setSchemaType(String schemaType) {
      this.schemaType = schemaType;
    }

    public List<String> getKeyTakeaway() {
      return keyTakeaway;
    }

    public void setKeyTakeaway(List<String> keyTakeaway) {
      this.keyTakeaway = keyTakeaway;
    }
  }
}
