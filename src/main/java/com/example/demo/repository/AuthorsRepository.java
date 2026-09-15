package com.example.demo.repository;

import com.example.demo.model.Authors;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthorsRepository extends MongoRepository<Authors, String> {
  long countBy();
}
