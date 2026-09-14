/*
 * (C) Copyright  Hyland (http://hyland.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Abhigyan Majumder
 */
package com.example.demo.model;

import com.example.demo.model.enums.ArticleStatus;
import com.example.demo.model.enums.ArticleType;
import com.example.demo.model.enums.PublicationSource;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "articles")
public class Articles {
  @Id private String id;

  private LocalizedText title;
  private String authorId;
  private ArticleType articleType;
  private PublicationSource publicationSource;
  private String printIssueId;
  private Instant publicationDate;
  private String featuredImageUrl;
  private LocalizedText featuredImageAlt;
  private Video video;
  private Seo seo;
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

  public LocalizedText getTitle() {
    return title;
  }

  public void setTitle(LocalizedText title) {
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

  public LocalizedText getFeaturedImageAlt() {
    return featuredImageAlt;
  }

  public void setFeaturedImageAlt(LocalizedText featuredImageAlt) {
    this.featuredImageAlt = featuredImageAlt;
  }

  public Video getVideo() {
    return video;
  }

  public void setVideo(Video video) {
    this.video = video;
  }

  public Seo getSeo() {
    return seo;
  }

  public void setSeo(Seo seo) {
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
