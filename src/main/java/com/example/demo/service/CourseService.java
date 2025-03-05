package com.example.demo.service;

import com.example.demo.model.Courses;
import com.example.demo.model.modelDTO.CourseDTO;

public interface CourseService {
    CourseDTO addCourses(Courses courses);

    CourseDTO deleteCourse(String id);

    CourseDTO getCourse(String id);

    CourseDTO updateCourse(String id, Courses courses);
}
