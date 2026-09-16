package com.example.demo.repository;

import com.example.demo.model.Authors;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface AuthorsRepository extends MongoRepository<Authors, String> {
  @Query(value = "{ 'deletedAt': null }", count = true)
  long countByDeletedAtIsNull();

  @Query("{ '_id': ?0, 'deletedAt': null }")
  Optional<Authors> findByIdAndNotDeleted(String id);

  @Query("{ 'deletedAt': null }")
  Page<Authors> findAllByDeletedAtIsNull(Pageable pageable);
}
