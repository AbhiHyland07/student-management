package com.example.demo.repository;

import com.example.demo.model.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TeacherRepository extends MongoRepository<Teacher,String> {
    Optional<Teacher> findByUsername(String username);
}
