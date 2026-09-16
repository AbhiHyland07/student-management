package com.example.demo.repository;

import com.example.demo.model.PrintIssues;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PrintIssuesRepository extends MongoRepository<PrintIssues, String> {
  @Query(value = "{ 'deletedAt': null }", sort = "{ 'publicationDate': -1 }")
  List<PrintIssues> findTopByOrderByPublicationDateDesc(Pageable pageable);

  @Query(value = "{ 'deletedAt': null }", count = true)
  long countByDeletedAtIsNull();

  @Query("{ '_id': ?0, 'deletedAt': null }")
  Optional<PrintIssues> findByIdAndNotDeleted(String id);

  @Query("{ 'deletedAt': null }")
  Page<PrintIssues> findAllByDeletedAtIsNull(Pageable pageable);
}
