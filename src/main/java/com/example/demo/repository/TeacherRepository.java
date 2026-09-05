package com.example.demo.repository;

import com.example.demo.model.Teacher;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeacherRepository extends MongoRepository<Teacher, String> {
  Optional<Teacher> findByUsername(String username);
}
