package com.example.demo.controller;

import com.example.demo.model.Courses;
import com.example.demo.model.modelDTO.CourseDTO;
import com.example.demo.service.CourseService;
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
@RequestMapping("/api/course")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @Operation(summary = "Creating a course and store it in database", description = "This accept course object as a request body", parameters = {
            @Parameter(name = "Courses", description = "Accept a Course user Object", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Courses.class)))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the CourseDTO Dto", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CourseDTO.class))),
            @ApiResponse(responseCode = "302", description = "This throw's when course is already present.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.example.demo.exception.ApiResponse.class)))
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody Courses courses) {
        return ResponseEntity.ok(courseService.addCourses(courses));
    }

    @Operation(summary = "Deleting a course from database", description = "This accept course id as a Path variable and deletes that particular course form database and its corresponding student and teacher having this course is updated.",parameters = {
            @Parameter(name = "id", description = "The course ID of the course", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the Course", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CourseDTO.class))),
            @ApiResponse(responseCode = "404", description = "This throw's when course is not present.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.example.demo.exception.ApiResponse.class)))
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable(name = "id") String id){
        return ResponseEntity.ok(courseService.deleteCourse(id));
    }

    @Operation(summary = "Getting a course from database", description = "This accept course id as a Path variable and returns the course.", parameters = {
            @Parameter(name = "id", description = "The course ID of the course", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the Course", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CourseDTO.class))),
            @ApiResponse(responseCode = "404", description = "This throw's when course is not present.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.example.demo.exception.ApiResponse.class)))
    })
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('STUDENT') or hasAuthority('TEACHER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCourse(@PathVariable String id){
        return ResponseEntity.ok(courseService.getCourse(id));
    }

    @Operation(summary = "Updating a course from database", description = "This accept course id as a Path variable and updates that particular course form database and its corresponding student and teacher having this course is updated.", parameters = {
            @Parameter(name = "id", description = "The course ID of the course", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the Course", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CourseDTO.class))),
            @ApiResponse(responseCode = "404", description = "This throw's when course is not present.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.example.demo.exception.ApiResponse.class)))
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable String id, @RequestBody Courses courses){
        return ResponseEntity.ok(courseService.updateCourse(id,courses));
    }
}
