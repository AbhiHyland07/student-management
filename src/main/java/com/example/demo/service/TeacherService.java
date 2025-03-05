package com.example.demo.service;

import com.example.demo.model.Teacher;
import com.example.demo.model.modelDTO.TeacherDTO;

public interface TeacherService {
    TeacherDTO addTeacher(Teacher teacher, String id);
}
