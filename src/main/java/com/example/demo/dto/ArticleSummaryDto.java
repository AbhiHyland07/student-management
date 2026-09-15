package com.example.demo.dto;

import com.example.demo.model.enums.ArticleStatus;
import com.example.demo.model.enums.ArticleType;
import com.example.demo.model.enums.PublicationSource;

public class ArticleSummaryDto {
  private String id;
  private LocalizedTextDto title;
  private PublicationSource publicationSource;
  private ArticleType articleType;
  private ArticleStatus status;
  private String featuredImageUrl;

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

  public PublicationSource getPublicationSource() {
    return publicationSource;
  }

  public void setPublicationSource(PublicationSource publicationSource) {
    this.publicationSource = publicationSource;
  }

  public ArticleType getArticleType() {
    return articleType;
  }

  public void setArticleType(ArticleType articleType) {
    this.articleType = articleType;
  }

  public String getFeaturedImageUrl() {
    return featuredImageUrl;
  }

  public void setFeaturedImageUrl(String featuredImageUrl) {
    this.featuredImageUrl = featuredImageUrl;
  }

  public ArticleStatus getStatus() {
    return status;
  }

  public void setStatus(ArticleStatus status) {
    this.status = status;
  }
}
