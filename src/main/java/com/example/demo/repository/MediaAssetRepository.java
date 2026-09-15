package com.example.demo.repository;

import com.example.demo.model.MediaAsset;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MediaAssetRepository extends MongoRepository<MediaAsset, String> {
  long countBy();
}
