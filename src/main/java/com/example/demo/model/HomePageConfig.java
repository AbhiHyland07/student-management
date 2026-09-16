package com.example.demo.model;

import com.example.demo.model.enums.LatestUpdatedSource;
import java.time.Instant;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "homePageConfig")
public class HomePageConfig {
  @Id private String id;

  private String mainArticleId;
  private LatestUpdatedSource latestUpdatedSource;
  private Integer latestUpdatedCount;
  private Boolean videoSectionEnabled;
  private List<String> featureArticleIds;
  private String featurePrintIssueId;
  private Boolean featurePrintIssueIsAutoLatest;
  private Instant updatedAt;
  private Instant deletedAt;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getMainArticleId() {
    return mainArticleId;
  }

  public void setMainArticleId(String mainArticleId) {
    this.mainArticleId = mainArticleId;
  }

  public LatestUpdatedSource getLatestUpdatedSource() {
    return latestUpdatedSource;
  }

  public void setLatestUpdatedSource(LatestUpdatedSource latestUpdatedSource) {
    this.latestUpdatedSource = latestUpdatedSource;
  }

  public Integer getLatestUpdatedCount() {
    return latestUpdatedCount;
  }

  public void setLatestUpdatedCount(Integer latestUpdatedCount) {
    this.latestUpdatedCount = latestUpdatedCount;
  }

  public Boolean getVideoSectionEnabled() {
    return videoSectionEnabled;
  }

  public void setVideoSectionEnabled(Boolean videoSectionEnabled) {
    this.videoSectionEnabled = videoSectionEnabled;
  }

  public List<String> getFeatureArticleIds() {
    return featureArticleIds;
  }

  public void setFeatureArticleIds(List<String> featureArticleIds) {
    this.featureArticleIds = featureArticleIds;
  }

  public String getFeaturePrintIssueId() {
    return featurePrintIssueId;
  }

  public void setFeaturePrintIssueId(String featurePrintIssueId) {
    this.featurePrintIssueId = featurePrintIssueId;
  }

  public Boolean getFeaturePrintIssueIsAutoLatest() {
    return featurePrintIssueIsAutoLatest;
  }

  public void setFeaturePrintIssueIsAutoLatest(Boolean featurePrintIssueIsAutoLatest) {
    this.featurePrintIssueIsAutoLatest = featurePrintIssueIsAutoLatest;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Instant getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(Instant deletedAt) {
    this.deletedAt = deletedAt;
  }

  public boolean isDeleted() {
    return deletedAt != null;
  }
}
