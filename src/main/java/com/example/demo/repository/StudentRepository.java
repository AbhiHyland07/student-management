package com.example.demo.repository;

import com.example.demo.model.Student;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
  Optional<Student> findByUsername(String username);
}
