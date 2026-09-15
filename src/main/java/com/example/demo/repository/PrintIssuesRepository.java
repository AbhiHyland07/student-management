package com.example.demo.repository;

import com.example.demo.model.PrintIssues;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PrintIssuesRepository extends MongoRepository<PrintIssues, String> {
  Optional<PrintIssues> findTopByOrderByPublicationDateDesc();

  long countBy();
}
