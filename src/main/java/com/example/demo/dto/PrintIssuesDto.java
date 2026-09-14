package com.example.demo.dto;

import java.time.Instant;

public class PrintIssuesDto {
  private String id;
  private Integer volumeNumber;
  private Integer issueNumber;
  private Instant publicationDate;
  private String coverImageUrl;
  private String pdfUrl;
  private Long pdfFileSizeBytes;
  private Boolean isPublished;
  private Instant createdAt;
  private Instant updatedAt;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Integer getVolumeNumber() {
    return volumeNumber;
  }

  public void setVolumeNumber(Integer volumeNumber) {
    this.volumeNumber = volumeNumber;
  }

  public Integer getIssueNumber() {
    return issueNumber;
  }

  public void setIssueNumber(Integer issueNumber) {
    this.issueNumber = issueNumber;
  }

  public Instant getPublicationDate() {
    return publicationDate;
  }

  public void setPublicationDate(Instant publicationDate) {
    this.publicationDate = publicationDate;
  }

  public String getCoverImageUrl() {
    return coverImageUrl;
  }

  public void setCoverImageUrl(String coverImageUrl) {
    this.coverImageUrl = coverImageUrl;
  }

  public String getPdfUrl() {
    return pdfUrl;
  }

  public void setPdfUrl(String pdfUrl) {
    this.pdfUrl = pdfUrl;
  }

  public Long getPdfFileSizeBytes() {
    return pdfFileSizeBytes;
  }

  public void setPdfFileSizeBytes(Long pdfFileSizeBytes) {
    this.pdfFileSizeBytes = pdfFileSizeBytes;
  }

  public Boolean getIsPublished() {
    return isPublished;
  }

  public void setIsPublished(Boolean isPublished) {
    this.isPublished = isPublished;
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
}
