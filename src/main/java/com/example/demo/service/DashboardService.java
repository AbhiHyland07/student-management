package com.example.demo.service;

import com.example.demo.dto.DashboardSummaryDto;
import com.example.demo.mapper.ArticlesMapper;
import com.example.demo.mapper.PrintIssuesMapper;
import com.example.demo.model.enums.ArticleStatus;
import com.example.demo.repository.ArticlesRepository;
import com.example.demo.repository.AuthorsRepository;
import com.example.demo.repository.PrintIssuesRepository;
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
    summary.setPublishedArticleCount(articlesRepository.countByStatus(ArticleStatus.PUBLISHED));
    summary.setDraftArticleCounts(articlesRepository.countByStatus(ArticleStatus.DRAFT));
    summary.setAuthorCounts(authorsRepository.countBy());
    summary.setPrintIssueCount(printIssuesRepository.countBy());
    summary.setRecentArticles(
        articlesRepository.findTop5ByOrderByCreatedAtDesc().stream()
            .map(ArticlesMapper::toDto)
            .toList());
    summary.setLatestPrintIssue(
        printIssuesRepository
            .findTopByOrderByPublicationDateDesc()
            .map(PrintIssuesMapper::toDto)
            .orElse(null));
    return summary;
  }
}
