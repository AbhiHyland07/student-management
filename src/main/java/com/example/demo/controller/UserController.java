package com.example.demo.controller;

import com.example.demo.dto.CreateUserRequestDto;
import com.example.demo.dto.InviteUserRequestDto;
import com.example.demo.dto.UpdateUserRequestDto;
import com.example.demo.dto.UserListResponseDto;
import com.example.demo.dto.UsersDto;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
@Tag(name = "User Management", description = "APIs for creating, updating, and managing users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @Operation(summary = "Get users", description = "Returns paginated users with optional search.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Users fetched successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserListResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('user:view')")
  @GetMapping
  public ResponseEntity<UserListResponseDto> getUsers(
      @Parameter(description = "Page number, starting from 1") @RequestParam(required = false)
          Integer page,
      @Parameter(description = "Page size, default is 20") @RequestParam(required = false)
          Integer pageSize,
      @Parameter(description = "Search text for full name or email") @RequestParam(required = false)
          String search) {
    return ResponseEntity.ok(userService.getUsers(page, pageSize, search));
  }

  @Operation(summary = "Create a new user", description = "Creates a new active user.")
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
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('user:manage')")
  @PostMapping
  public ResponseEntity<UsersDto> createUser(@Valid @RequestBody CreateUserRequestDto user) {
    UsersDto createdUser = userService.createUser(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  @Operation(summary = "Invite user", description = "Creates an invited user without password.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "User invited successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UsersDto.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data or user already exists"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('user:manage')")
  @PostMapping("/invite")
  public ResponseEntity<UsersDto> inviteUser(@Valid @RequestBody InviteUserRequestDto user) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.inviteUser(user));
  }

  @Operation(
      summary = "Update user",
      description = "Updates user details and optionally creates an author linked to the user.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "User updated successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UsersDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "User not found")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('user:manage')")
  @PutMapping("/{id}")
  public ResponseEntity<UsersDto> updateUser(
      @PathVariable String id, @Valid @RequestBody UpdateUserRequestDto user) {
    return ResponseEntity.ok(userService.updateUser(id, user));
  }

  @Operation(summary = "Suspend user", description = "Suspends a user.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "User suspended successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "User not found")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('user:manage')")
  @PostMapping("/{id}/suspend")
  public ResponseEntity<Void> suspendUser(@PathVariable String id) {
    userService.suspendUser(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Reactivate user", description = "Reactivates a suspended or invited user.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "User reactivated successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "User not found")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('ADMIN') and hasAuthority('user:manage')")
  @PostMapping("/{id}/reactivate")
  public ResponseEntity<Void> reactivateUser(@PathVariable String id) {
    userService.reactivateUser(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Delete user", description = "Deletes a user by id.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "User not found")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('ADMIN') and hasAuthority('user:manage')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable String id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
