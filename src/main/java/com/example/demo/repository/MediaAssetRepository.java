package com.example.demo.repository;

import com.example.demo.model.MediaAsset;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MediaAssetRepository extends MongoRepository<MediaAsset, String> {
  @Query("{ '_id': ?0, 'deletedAt': null }")
  Optional<MediaAsset> findByIdAndNotDeleted(String id);

  @Query(value = "{ 'deletedAt': null }", count = true)
  Long countAll();

  @Query("{ 'deletedAt': null }")
  Page<MediaAsset> findAllByDeletedAtIsNull(Pageable pageable);
}
