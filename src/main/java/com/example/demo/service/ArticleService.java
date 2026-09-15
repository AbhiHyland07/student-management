package com.example.demo.service;

import com.example.demo.dto.ArticleListResponseDto;
import com.example.demo.dto.ArticleSummaryDto;
import com.example.demo.dto.ArticlesDto;
import com.example.demo.exception.model.ResourceNotFound;
import com.example.demo.mapper.ArticlesMapper;
import com.example.demo.mapper.LocalizedTextMapper;
import com.example.demo.model.Articles;
import com.example.demo.model.enums.ArticleStatus;
import com.example.demo.model.enums.ArticleType;
import com.example.demo.model.enums.PublicationSource;
import com.example.demo.repository.ArticlesRepository;
import com.example.demo.repository.AuthorsRepository;
import com.example.demo.repository.PrintIssuesRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
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

  public ArticleService(
      MongoTemplate mongoTemplate,
      ArticlesRepository articlesRepository,
      AuthorsRepository authorsRepository,
      PrintIssuesRepository printIssuesRepository) {
    this.mongoTemplate = mongoTemplate;
    this.articlesRepository = articlesRepository;
    this.authorsRepository = authorsRepository;
    this.printIssuesRepository = printIssuesRepository;
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

    long total = mongoTemplate.count(new Query(), Articles.class);

    query.with(Sort.by(direction, resolvedSortBy));
    query.skip((long) (resolvedPage - 1) * resolvedPageSize);
    query.limit(resolvedPageSize);

    List<ArticlesDto> items =
        mongoTemplate.find(query, Articles.class).stream().map(ArticlesMapper::toDto).toList();

    ArticleListResponseDto response = new ArticleListResponseDto();
    response.setItems(items);
    response.setTotal(total);
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
        .findById(id)
        .map(ArticlesMapper::toDto)
        .orElseThrow(() -> new ResourceNotFound("Article not found"));
  }

  public ArticleSummaryDto createArticle(ArticlesDto articleDto) {
    Articles article = ArticlesMapper.toModel(articleDto);
    validateReferences(article);
    Instant now = Instant.now();
    article.setCreatedAt(now);
    article.setUpdatedAt(now);
    if (article.getStatus() == ArticleStatus.PUBLISHED && article.getPublishedAt() == null) {
      article.setPublishedAt(now);
    }
    Articles savedArticle = articlesRepository.save(article);
    return toSummaryDto(savedArticle);
  }

  public void updateArticle(String id, ArticlesDto articleDto) {
    Articles existingArticle =
        articlesRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFound("Article not found"));
    Articles updatedArticle = ArticlesMapper.toModel(articleDto);
    updatedArticle.setId(existingArticle.getId());
    validateReferences(updatedArticle);
    updatedArticle.setCreatedAt(existingArticle.getCreatedAt());
    updatedArticle.setUpdatedAt(Instant.now());
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
    Articles existingArticle =
        articlesRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFound("Article not found"));
    articlesRepository.delete(existingArticle);
  }

  public ArticlesDto duplicateArticle(String id) {
    Articles existingArticle =
        articlesRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFound("Article not found"));
    Articles duplicatedArticle = ArticlesMapper.toModel(ArticlesMapper.toDto(existingArticle));
    Instant now = Instant.now();
    duplicatedArticle.setStatus(ArticleStatus.DRAFT);
    duplicatedArticle.setCreatedAt(now);
    duplicatedArticle.setUpdatedAt(now);
    duplicatedArticle.setPublishedAt(null);
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
    if (article.getAuthorId() != null
        && !article.getAuthorId().isBlank()
        && authorsRepository.findById(article.getAuthorId()).isEmpty()) {
      throw new ResourceNotFound("Author not found");
    }
    if (article.getPrintIssueId() != null
        && !article.getPrintIssueId().isBlank()
        && printIssuesRepository.findById(article.getPrintIssueId()).isEmpty()) {
      throw new ResourceNotFound("Print issue not found");
    }
  }
}
