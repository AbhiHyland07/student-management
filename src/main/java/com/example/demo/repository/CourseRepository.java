package com.example.demo.repository;

import com.example.demo.model.Courses;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends MongoRepository<Courses, String> {
  Optional<Courses> findByCourseId(String courseID);
}
