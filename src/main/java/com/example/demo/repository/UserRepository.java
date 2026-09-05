package com.example.demo.repository;

import com.example.demo.model.UserName;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserName, String> {
  Optional<UserName> findByUsername(String username);
}
