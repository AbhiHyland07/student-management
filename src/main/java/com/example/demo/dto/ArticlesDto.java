package com.example.demo.dto;

import com.example.demo.model.enums.ArticleStatus;
import com.example.demo.model.enums.ArticleType;
import com.example.demo.model.enums.PublicationSource;
import java.time.Instant;

public class ArticlesDto {
  private String id;
  private LocalizedTextDto title;
  private String authorId;
  private ArticleType articleType;
  private PublicationSource publicationSource;
  private String printIssueId;
  private Instant publicationDate;
  private String featuredImageUrl;
  private LocalizedTextDto featuredImageAlt;
  private VideoDto video;
  private SeoDto seo;
  private ArticleStatus status;
  private Instant createdAt;
  private Instant updatedAt;
  private Instant publishedAt;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LocalizedTextDto getTitle() {
    return title;
  }

  public void setTitle(LocalizedTextDto title) {
    this.title = title;
  }

  public String getAuthorId() {
    return authorId;
  }

  public void setAuthorId(String authorId) {
    this.authorId = authorId;
  }

  public ArticleType getArticleType() {
    return articleType;
  }

  public void setArticleType(ArticleType articleType) {
    this.articleType = articleType;
  }

  public PublicationSource getPublicationSource() {
    return publicationSource;
  }

  public void setPublicationSource(PublicationSource publicationSource) {
    this.publicationSource = publicationSource;
  }

  public String getPrintIssueId() {
    return printIssueId;
  }

  public void setPrintIssueId(String printIssueId) {
    this.printIssueId = printIssueId;
  }

  public Instant getPublicationDate() {
    return publicationDate;
  }

  public void setPublicationDate(Instant publicationDate) {
    this.publicationDate = publicationDate;
  }

  public String getFeaturedImageUrl() {
    return featuredImageUrl;
  }

  public void setFeaturedImageUrl(String featuredImageUrl) {
    this.featuredImageUrl = featuredImageUrl;
  }

  public LocalizedTextDto getFeaturedImageAlt() {
    return featuredImageAlt;
  }

  public void setFeaturedImageAlt(LocalizedTextDto featuredImageAlt) {
    this.featuredImageAlt = featuredImageAlt;
  }

  public VideoDto getVideo() {
    return video;
  }

  public void setVideo(VideoDto video) {
    this.video = video;
  }

  public SeoDto getSeo() {
    return seo;
  }

  public void setSeo(SeoDto seo) {
    this.seo = seo;
  }

  public ArticleStatus getStatus() {
    return status;
  }

  public void setStatus(ArticleStatus status) {
    this.status = status;
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

  public Instant getPublishedAt() {
    return publishedAt;
  }

  public void setPublishedAt(Instant publishedAt) {
    this.publishedAt = publishedAt;
  }
}
