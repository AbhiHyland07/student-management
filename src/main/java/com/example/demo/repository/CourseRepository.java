package com.example.demo.repository;

import com.example.demo.model.Courses;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends MongoRepository<Courses,String> {
    Optional<Courses> findByCourseId(String courseID);
}
