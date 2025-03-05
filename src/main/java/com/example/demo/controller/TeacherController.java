package com.example.demo.controller;

import com.example.demo.model.Teacher;
import com.example.demo.model.modelDTO.TeacherDTO;
import com.example.demo.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/course/{id}")
    public ResponseEntity<?> createTeacher(@RequestBody Teacher teacher, @PathVariable String id) {
        return ResponseEntity.ok(teacherService.addTeacher(teacher,id));
    }
}
