package com.example.demo.controller;

import com.example.demo.dto.UsersDto;
import com.example.demo.model.Users;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
@Tag(name = "User Management", description = "APIs for creating, updating, and managing users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @Operation(
      summary = "Create a new user",
      description =
          "Creates a new user in the system with provided details. Password will be hashed before"
              + " storage.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "User created successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UsersDto.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data or user already exists"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
      })
  @PostMapping
  public ResponseEntity<UsersDto> createUser(@Valid @RequestBody Users user) {
    UsersDto createdUser = userService.createUser(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  @Operation(summary = "Get user by ID", description = "Retrieves a specific user by their ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "User found successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UsersDto.class))),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @GetMapping("/{id}")
  public ResponseEntity<UsersDto> getUserById(@PathVariable String id) {
    UsersDto user = userService.getUserById(id);
    return ResponseEntity.ok(user);
  }

  @Operation(summary = "Get all users", description = "Retrieves a list of all users in the system")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Users retrieved successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UsersDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @GetMapping
  public ResponseEntity<List<UsersDto>> getAllUsers() {
    List<UsersDto> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }

  @Operation(
      summary = "Update user",
      description =
          "Updates an existing user with the provided details. Only non-null fields will be"
              + " updated.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "User updated successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UsersDto.class))),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PutMapping("/{id}")
  public ResponseEntity<UsersDto> updateUser(
      @PathVariable String id, @Valid @RequestBody Users userUpdates) {
    UsersDto updatedUser = userService.updateUser(id, userUpdates);
    return ResponseEntity.ok(updatedUser);
  }

  @Operation(summary = "Delete user", description = "Deletes a user from the system")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable String id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
