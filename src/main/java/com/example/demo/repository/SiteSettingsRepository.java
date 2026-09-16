package com.example.demo.repository;

import com.example.demo.model.SiteSettings;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface SiteSettingsRepository extends MongoRepository<SiteSettings, String> {
  @Query(value = "{ 'deletedAt': null }", count = true)
  long countAll();

  @Query("{ 'deletedAt': null }")
  List<SiteSettings> findAllByDeletedAtIsNull();
}
