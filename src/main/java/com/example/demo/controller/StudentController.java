package com.example.demo.controller;

import com.example.demo.model.Student;
import com.example.demo.model.abstractModel.User;
import com.example.demo.model.modelDTO.StudentDTO;
import com.example.demo.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @Operation(summary = "Creating a student", description = "Creating a student object and passing as a request payload then storing it in the database", parameters = {
            @Parameter(name = "Student", description = "Accept a Student user Object", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = User.class)))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the Student Dto", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = StudentDTO.class))),
            @ApiResponse(responseCode = "302", description = "Student is already present", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.example.demo.exception.ApiResponse.class)))
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createStudentUser(@RequestBody User student) {
        return ResponseEntity.ok(studentService.addStudent((Student) student));
    }

    @Operation(summary = "Adding course to a student", description = "This accept username and course id as a path variable after that the course get added to the students course list field", parameters = {
            @Parameter(name = "id", description = "The course ID of the course", required = true),
            @Parameter(name = "username", description = "The username of the student", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the Student Dto", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = StudentDTO.class))),
            @ApiResponse(responseCode = "404", description = "This throw's when either the student username or the course id is not found.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.example.demo.exception.ApiResponse.class)))
    })
    @PreAuthorize("#username == authentication.principal.username  or hasAuthority('ADMIN') ")
    @PutMapping("/register/{username}/{id}")
    public ResponseEntity<?> addCourse(@PathVariable(name = "username") String username,
                             @PathVariable(name = "id") String courseId){
        return ResponseEntity.ok(studentService.updateCourse(username,courseId));
    }

    @Operation(summary = "Fetching a Student Details", description = "This accept username as a path variable after that the corresponding student is fetched", parameters = {
            @Parameter(name = "username", description = "Accept a username of student", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the Student Dto", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = StudentDTO.class))),
            @ApiResponse(responseCode = "404", description = "This throw's when either the student username  is not found.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.example.demo.exception.ApiResponse.class)))
    })
    @PreAuthorize("#username == authentication.principal.username  or hasAuthority('ADMIN') or hasAuthority('TEACHER')")
    @GetMapping("/{username}")
    public ResponseEntity<?> getStudent(@PathVariable(name = "username") String username){
        return ResponseEntity.ok(studentService.getStudent(username));
    }
}
