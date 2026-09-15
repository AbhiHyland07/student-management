package com.example.demo.controller;

import com.example.demo.dto.AuthorListResponseDto;
import com.example.demo.dto.AuthorsDto;
import com.example.demo.service.AuthorService;
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
@RequestMapping("authors")
@Tag(name = "Authors", description = "Author APIs")
public class AuthorController {
  private final AuthorService authorService;

  public AuthorController(AuthorService authorService) {
    this.authorService = authorService;
  }

  @Operation(
      summary = "Get authors",
      description = "Returns paginated authors with optional search.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Authors fetched successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AuthorListResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('author:view')")
  @GetMapping
  public ResponseEntity<AuthorListResponseDto> getAuthors(
      @Parameter(description = "Page number, starting from 1") @RequestParam(required = false)
          Integer page,
      @Parameter(description = "Page size, default is 20") @RequestParam(required = false)
          Integer pageSize,
      @Parameter(description = "Search text for author fields") @RequestParam(required = false)
          String search) {
    return ResponseEntity.ok(authorService.getAuthors(page, pageSize, search));
  }

  @Operation(summary = "Create author", description = "Creates a new author.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Author created successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AuthorsDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('author:manage')")
  @PostMapping
  public ResponseEntity<AuthorsDto> createAuthor(@Valid @RequestBody AuthorsDto authorsDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authorService.createAuthor(authorsDto));
  }

  @Operation(summary = "Update author", description = "Updates an existing author.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Author updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Author not found")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('author:manage')")
  @PutMapping("/{id}")
  public ResponseEntity<Void> updateAuthor(
      @PathVariable String id, @Valid @RequestBody AuthorsDto authorsDto) {
    authorService.updateAuthor(id, authorsDto);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Delete author", description = "Deletes an author by id.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Author deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Author not found")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('author:manage')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAuthor(@PathVariable String id) {
    authorService.deleteAuthor(id);
    return ResponseEntity.noContent().build();
  }
}
