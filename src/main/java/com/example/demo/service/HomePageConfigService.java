package com.example.demo.service;

import com.example.demo.dto.HomePageConfigDto;
import com.example.demo.exception.model.ResourceNotFound;
import com.example.demo.mapper.HomePageConfigMapper;
import com.example.demo.model.HomePageConfig;
import com.example.demo.repository.ArticlesRepository;
import com.example.demo.repository.HomePageConfigRepository;
import com.example.demo.repository.PrintIssuesRepository;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class HomePageConfigService {
  private final HomePageConfigRepository homePageConfigRepository;
  private final ArticlesRepository articlesRepository;
  private final PrintIssuesRepository printIssuesRepository;

  public HomePageConfigService(
      HomePageConfigRepository homePageConfigRepository,
      ArticlesRepository articlesRepository,
      PrintIssuesRepository printIssuesRepository) {
    this.homePageConfigRepository = homePageConfigRepository;
    this.articlesRepository = articlesRepository;
    this.printIssuesRepository = printIssuesRepository;
  }

  public HomePageConfigDto getHomePageConfig() {
    return homePageConfigRepository.findAllByDeletedAtIsNull().stream()
        .findFirst()
        .map(HomePageConfigMapper::toDto)
        .orElse(null);
  }

  public HomePageConfigDto updateHomePageConfig(HomePageConfigDto homePageConfigDto) {
    HomePageConfig existingConfig =
        homePageConfigRepository.findAllByDeletedAtIsNull().stream().findFirst().orElse(null);
    HomePageConfig config = HomePageConfigMapper.toModel(homePageConfigDto);
    validateReferences(config);
    if (existingConfig != null) {
      config.setId(existingConfig.getId());
      config.setDeletedAt(existingConfig.getDeletedAt());
    } else {
      config.setId(null);
      config.setDeletedAt(null);
    }
    config.setUpdatedAt(Instant.now());
    return HomePageConfigMapper.toDto(homePageConfigRepository.save(config));
  }

  private void validateReferences(HomePageConfig config) {
    if (config.getMainArticleId() != null
        && !config.getMainArticleId().isBlank()
        && articlesRepository.findByIdAndNotDeleted(config.getMainArticleId()).isEmpty()) {
      throw new ResourceNotFound("Main article not found or is deleted");
    }
    if (config.getFeatureArticleIds() != null) {
      for (String articleId : config.getFeatureArticleIds()) {
        if (articleId != null
            && !articleId.isBlank()
            && articlesRepository.findByIdAndNotDeleted(articleId).isEmpty()) {
          throw new ResourceNotFound("Featured article not found or is deleted");
        }
      }
    }
    if (config.getFeaturePrintIssueId() != null
        && !config.getFeaturePrintIssueId().isBlank()
        && printIssuesRepository.findByIdAndNotDeleted(config.getFeaturePrintIssueId()).isEmpty()) {
      throw new ResourceNotFound("Feature print issue not found or is deleted");
    }
  }
}
