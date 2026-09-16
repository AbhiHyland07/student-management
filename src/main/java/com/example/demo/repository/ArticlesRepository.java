package com.example.demo.repository;

import com.example.demo.model.Articles;
import com.example.demo.model.enums.ArticleStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ArticlesRepository extends MongoRepository<Articles, String> {

  @Query(value = "{ 'status': ?0, 'deletedAt': null }", count = true)
  long countByStatusAndDeletedAtIsNull(ArticleStatus status);

  @Query(value = "{ 'deletedAt': null }", count = true)
  long countByDeletedAtIsNull();

  @Query(value = "{ 'deletedAt': null }", sort = "{ 'createdAt': -1 }")
  List<Articles> findTop5ByOrderByCreatedAtDesc(Pageable pageable);

  @Query("{ '_id': ?0, 'deletedAt': null }")
  Optional<Articles> findByIdAndNotDeleted(String id);

  @Query("{ 'deletedAt': null }")
  List<Articles> findAllByDeletedAtIsNull();

  @Query("{ 'deletedAt': null }")
  Page<Articles> findAllByDeletedAtIsNull(Pageable pageable);

  @Query(value = "{ 'authorId': ?0, 'deletedAt': null }", count = true)
  long countByAuthorIdAndNotDeleted(String authorId);

  @Query(value = "{ 'printIssueId': ?0, 'deletedAt': null }", count = true)
  long countByPrintIssueIdAndNotDeleted(String printIssueId);
}
