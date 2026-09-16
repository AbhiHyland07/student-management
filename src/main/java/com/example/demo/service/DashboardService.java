package com.example.demo.service;

import com.example.demo.dto.DashboardSummaryDto;
import com.example.demo.mapper.ArticlesMapper;
import com.example.demo.mapper.PrintIssuesMapper;
import com.example.demo.model.enums.ArticleStatus;
import com.example.demo.repository.ArticlesRepository;
import com.example.demo.repository.AuthorsRepository;
import com.example.demo.repository.PrintIssuesRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
  private final ArticlesRepository articlesRepository;
  private final AuthorsRepository authorsRepository;
  private final PrintIssuesRepository printIssuesRepository;

  public DashboardService(
      ArticlesRepository articlesRepository,
      AuthorsRepository authorsRepository,
      PrintIssuesRepository printIssuesRepository) {
    this.articlesRepository = articlesRepository;
    this.authorsRepository = authorsRepository;
    this.printIssuesRepository = printIssuesRepository;
  }

  public DashboardSummaryDto getSummary() {
    DashboardSummaryDto summary = new DashboardSummaryDto();
    summary.setPublishedArticleCount(
        articlesRepository.countByStatusAndDeletedAtIsNull(ArticleStatus.PUBLISHED));
    summary.setDraftArticleCounts(
        articlesRepository.countByStatusAndDeletedAtIsNull(ArticleStatus.DRAFT));
    summary.setAuthorCounts(authorsRepository.countByDeletedAtIsNull());
    summary.setPrintIssueCount(printIssuesRepository.countByDeletedAtIsNull());
    summary.setRecentArticles(
        articlesRepository
            .findTop5ByOrderByCreatedAtDesc(
                PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt")))
            .stream()
            .map(ArticlesMapper::toDto)
            .toList());
    summary.setLatestPrintIssue(
        printIssuesRepository
            .findTopByOrderByPublicationDateDesc(
                PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "publicationDate")))
            .stream()
            .map(PrintIssuesMapper::toDto)
            .findFirst()
            .orElse(null));
    return summary;
  }
}
