package com.example.demo.repository;

import com.example.demo.model.HomePageConfig;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface HomePageConfigRepository extends MongoRepository<HomePageConfig, String> {
  @Query(value = "{ 'deletedAt': null }", count = true)
  Long countAll();

  @Query("{ 'deletedAt': null }")
  List<HomePageConfig> findAllByDeletedAtIsNull();

  @Query(value = "{ 'mainArticleId': ?0, 'deletedAt': null }", count = true)
  long countByMainArticleIdAndNotDeleted(String articleId);

  @Query(value = "{ 'featureArticleIds': ?0, 'deletedAt': null }", count = true)
  long countByFeatureArticleIdAndNotDeleted(String articleId);

  @Query(value = "{ 'featurePrintIssueId': ?0, 'deletedAt': null }", count = true)
  long countByFeaturePrintIssueIdAndNotDeleted(String printIssueId);
}
