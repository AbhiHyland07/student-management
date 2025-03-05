package com.example.demo.controller;

import com.example.demo.model.Courses;
import com.example.demo.model.Student;
import com.example.demo.model.modelDTO.CourseDTO;
import com.example.demo.model.modelDTO.StudentDTO;
import com.example.demo.service.CourseService;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody Courses courses) {
        return ResponseEntity.ok(courseService.addCourses(courses));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable String id){
        return ResponseEntity.ok(courseService.deleteCourse(id));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('STUDENT') or hasAuthority('TEACHER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCourse(@PathVariable String id){
        return ResponseEntity.ok(courseService.getCourse(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable String id, @RequestBody Courses courses){
        return ResponseEntity.ok(courseService.updateCourse(id,courses));
    }
}
