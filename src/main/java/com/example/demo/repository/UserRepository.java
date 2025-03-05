package com.example.demo.repository;

import com.example.demo.model.Teacher;
import com.example.demo.model.UserName;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserName,String> {
    Optional<UserName> findByUsername(String username);
}
