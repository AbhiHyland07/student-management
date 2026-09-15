package com.example.demo.dto;

import java.util.List;

public class DashboardSummaryDto {
  private long publishedArticleCount;
  private long draftArticleCounts;
  private long authorCounts;
  private long printIssueCount;
  private List<ArticlesDto> recentArticles;
  private PrintIssuesDto latestPrintIssue;

  public long getPublishedArticleCount() {
    return publishedArticleCount;
  }

  public void setPublishedArticleCount(long publishedArticleCount) {
    this.publishedArticleCount = publishedArticleCount;
  }

  public long getDraftArticleCounts() {
    return draftArticleCounts;
  }

  public void setDraftArticleCounts(long draftArticleCounts) {
    this.draftArticleCounts = draftArticleCounts;
  }

  public long getAuthorCounts() {
    return authorCounts;
  }

  public void setAuthorCounts(long authorCounts) {
    this.authorCounts = authorCounts;
  }

  public long getPrintIssueCount() {
    return printIssueCount;
  }

  public void setPrintIssueCount(long printIssueCount) {
    this.printIssueCount = printIssueCount;
  }

  public List<ArticlesDto> getRecentArticles() {
    return recentArticles;
  }

  public void setRecentArticles(List<ArticlesDto> recentArticles) {
    this.recentArticles = recentArticles;
  }

  public PrintIssuesDto getLatestPrintIssue() {
    return latestPrintIssue;
  }

  public void setLatestPrintIssue(PrintIssuesDto latestPrintIssue) {
    this.latestPrintIssue = latestPrintIssue;
  }
}
