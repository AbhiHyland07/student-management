package com.example.demo.service;

import com.example.demo.dto.ArticleListResponseDto;
import com.example.demo.dto.ArticleSummaryDto;
import com.example.demo.dto.ArticlesDto;
import com.example.demo.exception.model.BusinessException;
import com.example.demo.exception.model.ResourceNotFound;
import com.example.demo.mapper.ArticlesMapper;
import com.example.demo.mapper.LocalizedTextMapper;
import com.example.demo.model.Articles;
import com.example.demo.model.enums.ArticleStatus;
import com.example.demo.model.enums.ArticleType;
import com.example.demo.model.enums.PublicationSource;
import com.example.demo.repository.ArticlesRepository;
import com.example.demo.repository.AuthorsRepository;
import com.example.demo.repository.HomePageConfigRepository;
import com.example.demo.repository.PrintIssuesRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
  private static final int DEFAULT_PAGE_SIZE = 20;

  private final MongoTemplate mongoTemplate;
  private final ArticlesRepository articlesRepository;
  private final AuthorsRepository authorsRepository;
  private final PrintIssuesRepository printIssuesRepository;
  private final HomePageConfigRepository homePageConfigRepository;

  public ArticleService(
      MongoTemplate mongoTemplate,
      ArticlesRepository articlesRepository,
      AuthorsRepository authorsRepository,
      PrintIssuesRepository printIssuesRepository,
      HomePageConfigRepository homePageConfigRepository) {
    this.mongoTemplate = mongoTemplate;
    this.articlesRepository = articlesRepository;
    this.authorsRepository = authorsRepository;
    this.printIssuesRepository = printIssuesRepository;
    this.homePageConfigRepository = homePageConfigRepository;
  }

  public ArticleListResponseDto getArticles(
      Integer page,
      Integer pageSize,
      String search,
      PublicationSource publicationSource,
      ArticleType articleType,
      ArticleStatus status,
      String authorId,
      String printIssueId,
      Boolean hasVideo,
      String sortBy,
      String sortDirection) {
    int resolvedPage = page == null || page < 1 ? 1 : page;
    int resolvedPageSize = pageSize == null || pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;
    String resolvedSortBy = isSortableField(sortBy) ? sortBy : "createdAt";
    Sort.Direction direction =
        "asc".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;

    List<Criteria> criteriaList = new ArrayList<>();
    criteriaList.add(Criteria.where("deletedAt").is(null));
    if (search != null && !search.isBlank()) {
      String escapedSearch = Pattern.quote(search.trim());
      criteriaList.add(
          new Criteria()
              .orOperator(
                  Criteria.where("title.en").regex(escapedSearch, "i"),
                  Criteria.where("title.bn").regex(escapedSearch, "i")));
    }
    if (publicationSource != null) {
      criteriaList.add(Criteria.where("publicationSource").is(publicationSource));
    }
    if (articleType != null) {
      criteriaList.add(Criteria.where("articleType").is(articleType));
    }
    if (status != null) {
      criteriaList.add(Criteria.where("status").is(status));
    }
    if (authorId != null && !authorId.isBlank()) {
      criteriaList.add(Criteria.where("authorId").is(authorId));
    }
    if (printIssueId != null && !printIssueId.isBlank()) {
      criteriaList.add(Criteria.where("printIssueId").is(printIssueId));
    }
    if (hasVideo != null) {
      criteriaList.add(
          hasVideo
              ? Criteria.where("video").ne(null)
              : new Criteria().orOperator(Criteria.where("video").is(null)));
    }

    Query query = new Query();
    if (!criteriaList.isEmpty()) {
      query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
    }

    query.with(Sort.by(direction, resolvedSortBy));
    query.skip((long) (resolvedPage - 1) * resolvedPageSize);
    query.limit(resolvedPageSize);

    List<ArticlesDto> items =
        mongoTemplate.find(query, Articles.class).stream().map(ArticlesMapper::toDto).toList();

    ArticleListResponseDto response = new ArticleListResponseDto();
    response.setItems(items);
    response.setTotal(response.getItems().size());
    response.setPage(resolvedPage);
    response.setPageSize(resolvedPageSize);
    return response;
  }

  public List<ArticleSummaryDto> getArticlesByIds(String ids) {
    if (ids == null || ids.isBlank()) {
      return List.of();
    }

    List<String> articleIds =
        Arrays.stream(ids.split(",")).map(String::trim).filter(id -> !id.isBlank()).toList();
    if (articleIds.isEmpty()) {
      return List.of();
    }

    Query query = new Query(Criteria.where("id").in(articleIds));
    return mongoTemplate.find(query, Articles.class).stream().map(this::toSummaryDto).toList();
  }

  public ArticlesDto getArticleById(String id) {
    return articlesRepository
        .findByIdAndNotDeleted(id)
        .map(ArticlesMapper::toDto)
        .orElseThrow(() -> new ResourceNotFound("Article not found"));
  }

  public ArticleSummaryDto createArticle(ArticlesDto articleDto) {
    Articles article = ArticlesMapper.toModel(articleDto);
    article.setId(null);
    validateReferences(article);
    Instant now = Instant.now();
    article.setCreatedAt(now);
    article.setUpdatedAt(now);
    article.setDeletedAt(null);
    if (article.getStatus() == ArticleStatus.PUBLISHED && article.getPublishedAt() == null) {
      article.setPublishedAt(now);
    }
    Articles savedArticle = articlesRepository.save(article);
    return toSummaryDto(savedArticle);
  }

  public void updateArticle(String id, ArticlesDto articleDto) {
    Articles existingArticle =
        articlesRepository
            .findByIdAndNotDeleted(id)
            .orElseThrow(() -> new ResourceNotFound("Article not found"));
    Articles updatedArticle = ArticlesMapper.toModel(articleDto);
    updatedArticle.setId(existingArticle.getId());
    validateReferences(updatedArticle);
    updatedArticle.setCreatedAt(existingArticle.getCreatedAt());
    updatedArticle.setUpdatedAt(Instant.now());
    updatedArticle.setDeletedAt(existingArticle.getDeletedAt());
    if (updatedArticle.getStatus() == ArticleStatus.PUBLISHED
        && updatedArticle.getPublishedAt() == null) {
      updatedArticle.setPublishedAt(
          existingArticle.getPublishedAt() != null
              ? existingArticle.getPublishedAt()
              : Instant.now());
    }
    articlesRepository.save(updatedArticle);
  }

  public void deleteArticle(String id) {
    Articles article =
        articlesRepository
            .findByIdAndNotDeleted(id)
            .orElseThrow(() -> new ResourceNotFound("Article not found"));

    // Check referential integrity
    long mainArticleCount = homePageConfigRepository.countByMainArticleIdAndNotDeleted(id);
    long featureArticleCount = homePageConfigRepository.countByFeatureArticleIdAndNotDeleted(id);

    if (mainArticleCount > 0 || featureArticleCount > 0) {
      throw new BusinessException(
          "Cannot delete article: referenced in "
              + mainArticleCount
              + " main articles and "
              + featureArticleCount
              + " feature articles in home page config");
    }

    // Soft delete
    article.setDeletedAt(Instant.now());
    articlesRepository.save(article);
  }

  public ArticlesDto duplicateArticle(String id) {
    Articles existingArticle =
        articlesRepository
            .findByIdAndNotDeleted(id)
            .orElseThrow(() -> new ResourceNotFound("Article not found"));
    Articles duplicatedArticle = ArticlesMapper.toModel(ArticlesMapper.toDto(existingArticle));
    Instant now = Instant.now();
    duplicatedArticle.setId(UUID.randomUUID().toString());
    duplicatedArticle.setStatus(ArticleStatus.DRAFT);
    duplicatedArticle.setCreatedAt(now);
    duplicatedArticle.setUpdatedAt(now);
    duplicatedArticle.setPublishedAt(null);
    duplicatedArticle.setDeletedAt(null);
    Articles savedArticle = articlesRepository.save(duplicatedArticle);
    return ArticlesMapper.toDto(savedArticle);
  }

  private ArticleSummaryDto toSummaryDto(Articles article) {
    ArticleSummaryDto dto = new ArticleSummaryDto();
    dto.setId(article.getId());
    dto.setTitle(LocalizedTextMapper.toDto(article.getTitle()));
    dto.setPublicationSource(article.getPublicationSource());
    dto.setArticleType(article.getArticleType());
    dto.setFeaturedImageUrl(article.getFeaturedImageUrl());
    dto.setStatus(article.getStatus());
    return dto;
  }

  private boolean isSortableField(String sortBy) {
    return "createdAt".equals(sortBy)
        || "updatedAt".equals(sortBy)
        || "publicationDate".equals(sortBy)
        || "publishedAt".equals(sortBy);
  }

  private void validateReferences(Articles article) {
    if (StringUtils.isBlank(article.getAuthorId())
        || StringUtils.isBlank(article.getPrintIssueId())) {
      throw new NullPointerException("Author ID and Print Issue ID cannot be null or blank");
    }
    if (article.getAuthorId() != null
        && !article.getAuthorId().isBlank()
        && authorsRepository.findByIdAndNotDeleted(article.getAuthorId()).isEmpty()) {
      throw new ResourceNotFound("Author not found or is deleted");
    }
    if (article.getPrintIssueId() != null
        && !article.getPrintIssueId().isBlank()
        && printIssuesRepository.findByIdAndNotDeleted(article.getPrintIssueId()).isEmpty()) {
      throw new ResourceNotFound("Print issue not found or is deleted");
    }
  }
}
