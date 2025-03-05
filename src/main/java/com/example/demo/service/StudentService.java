package com.example.demo.service;

import com.example.demo.model.Courses;
import com.example.demo.model.Student;
import com.example.demo.model.modelDTO.StudentDTO;

public interface StudentService {
    StudentDTO addStudent(Student student);

    StudentDTO updateCourse(String username, String courseId);

    StudentDTO getStudent(String username);
}
