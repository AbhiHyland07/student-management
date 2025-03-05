package com.example.demo.controller;

import com.example.demo.model.Student;
import com.example.demo.model.abstractModel.User;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createStudentUser(@RequestBody User student) {
        return ResponseEntity.ok(studentService.addStudent((Student) student));
    }

    @PreAuthorize("#username == authentication.principal.username  or hasAuthority('ADMIN')")
    @PutMapping("/register/{username}/{id}")
    public ResponseEntity<?> addCourse(@PathVariable(name = "username") String username,
                             @PathVariable(name = "id") String courseId){
        return ResponseEntity.ok(studentService.updateCourse(username,courseId));
    }

    @PreAuthorize("#username == authentication.principal.username  or hasAuthority('ADMIN')")
    @GetMapping("/{username}")
    public ResponseEntity<?> getStudent(@PathVariable(name = "username") String username){
        return ResponseEntity.ok(studentService.getStudent(username));
    }
}
