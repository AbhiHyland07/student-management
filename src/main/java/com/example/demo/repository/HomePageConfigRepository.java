package com.example.demo.repository;

import com.example.demo.model.HomePageConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HomePageConfigRepository extends MongoRepository<HomePageConfig, String> {}
