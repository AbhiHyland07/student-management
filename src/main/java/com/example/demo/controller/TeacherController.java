package com.example.demo.controller;

import com.example.demo.model.Teacher;
import com.example.demo.model.abstractModel.User;
import com.example.demo.model.modelDTO.TeacherDTO;
import com.example.demo.service.TeacherService;
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
@RequestMapping("/api/teacher")
public class TeacherController {
    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Operation(summary = "Fetching a Teacher Details", description = "This accept courseId as a path variable and teacher as RequestBody and returns TeacherDTO as a object", parameters = {
            @Parameter(name = "Teacher", description = "Accept a Teacher user Object", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = User.class))),
            @Parameter(name = "id", description = "Accept a CourseID as a path variable." , required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the Teacher Dto", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TeacherDTO.class))),
            @ApiResponse(responseCode = "404", description = "This throw's when course is not found.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.example.demo.exception.ApiResponse.class))),
            @ApiResponse(responseCode = "302", description = "This throw's when teacher already exist.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.example.demo.exception.ApiResponse.class)))
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/course/{id}")
    public ResponseEntity<?> createTeacher(@RequestBody Teacher teacher, @PathVariable(name = "id") String id) {
        return ResponseEntity.ok(teacherService.addTeacher(teacher,id));
    }
}
