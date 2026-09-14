package com.example.demo.mapper;

import com.example.demo.dto.HomePageConfigDto;
import com.example.demo.model.HomePageConfig;

public final class HomePageConfigMapper {
  private HomePageConfigMapper() {}

  public static HomePageConfigDto toDto(HomePageConfig config) {
    if (config == null) {
      return null;
    }
    HomePageConfigDto dto = new HomePageConfigDto();
    dto.setId(config.getId());
    dto.setMainArticleId(config.getMainArticleId());
    dto.setLatestUpdatedSource(config.getLatestUpdatedSource());
    dto.setLatestUpdatedCount(config.getLatestUpdatedCount());
    dto.setVideoSectionEnabled(config.getVideoSectionEnabled());
    dto.setFeatureArticleIds(config.getFeatureArticleIds());
    dto.setFeaturePrintIssueId(config.getFeaturePrintIssueId());
    dto.setFeaturePrintIssueIsAutoLatest(config.getFeaturePrintIssueIsAutoLatest());
    dto.setUpdatedAt(config.getUpdatedAt());
    return dto;
  }

  public static HomePageConfig toModel(HomePageConfigDto dto) {
    if (dto == null) {
      return null;
    }
    HomePageConfig config = new HomePageConfig();
    config.setId(dto.getId());
    config.setMainArticleId(dto.getMainArticleId());
    config.setLatestUpdatedSource(dto.getLatestUpdatedSource());
    config.setLatestUpdatedCount(dto.getLatestUpdatedCount());
    config.setVideoSectionEnabled(dto.getVideoSectionEnabled());
    config.setFeatureArticleIds(dto.getFeatureArticleIds());
    config.setFeaturePrintIssueId(dto.getFeaturePrintIssueId());
    config.setFeaturePrintIssueIsAutoLatest(dto.getFeaturePrintIssueIsAutoLatest());
    config.setUpdatedAt(dto.getUpdatedAt());
    return config;
  }
}
