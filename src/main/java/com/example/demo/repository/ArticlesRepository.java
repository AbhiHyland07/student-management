package com.example.demo.repository;

import com.example.demo.model.Articles;
import com.example.demo.model.enums.ArticleStatus;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArticlesRepository extends MongoRepository<Articles, String> {

  long countByStatus(ArticleStatus status);

  List<Articles> findTop5ByOrderByCreatedAtDesc();
}
