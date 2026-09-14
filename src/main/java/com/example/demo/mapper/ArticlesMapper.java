package com.example.demo.mapper;

import com.example.demo.dto.ArticlesDto;
import com.example.demo.model.Articles;

public final class ArticlesMapper {
  private ArticlesMapper() {}

  public static ArticlesDto toDto(Articles articles) {
    if (articles == null) {
      return null;
    }
    ArticlesDto dto = new ArticlesDto();
    dto.setId(articles.getId());
    dto.setTitle(LocalizedTextMapper.toDto(articles.getTitle()));
    dto.setAuthorId(articles.getAuthorId());
    dto.setArticleType(articles.getArticleType());
    dto.setPublicationSource(articles.getPublicationSource());
    dto.setPrintIssueId(articles.getPrintIssueId());
    dto.setPublicationDate(articles.getPublicationDate());
    dto.setFeaturedImageUrl(articles.getFeaturedImageUrl());
    dto.setFeaturedImageAlt(LocalizedTextMapper.toDto(articles.getFeaturedImageAlt()));
    dto.setVideo(VideoMapper.toDto(articles.getVideo()));
    dto.setSeo(SeoMapper.toDto(articles.getSeo()));
    dto.setStatus(articles.getStatus());
    dto.setCreatedAt(articles.getCreatedAt());
    dto.setUpdatedAt(articles.getUpdatedAt());
    dto.setPublishedAt(articles.getPublishedAt());
    return dto;
  }

  public static Articles toModel(ArticlesDto dto) {
    if (dto == null) {
      return null;
    }
    Articles articles = new Articles();
    articles.setId(dto.getId());
    articles.setTitle(LocalizedTextMapper.toModel(dto.getTitle()));
    articles.setAuthorId(dto.getAuthorId());
    articles.setArticleType(dto.getArticleType());
    articles.setPublicationSource(dto.getPublicationSource());
    articles.setPrintIssueId(dto.getPrintIssueId());
    articles.setPublicationDate(dto.getPublicationDate());
    articles.setFeaturedImageUrl(dto.getFeaturedImageUrl());
    articles.setFeaturedImageAlt(LocalizedTextMapper.toModel(dto.getFeaturedImageAlt()));
    articles.setVideo(VideoMapper.toModel(dto.getVideo()));
    articles.setSeo(SeoMapper.toModel(dto.getSeo()));
    articles.setStatus(dto.getStatus());
    articles.setCreatedAt(dto.getCreatedAt());
    articles.setUpdatedAt(dto.getUpdatedAt());
    articles.setPublishedAt(dto.getPublishedAt());
    return articles;
  }
}
