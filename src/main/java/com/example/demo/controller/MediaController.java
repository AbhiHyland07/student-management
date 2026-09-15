package com.example.demo.controller;

import com.example.demo.dto.MediaAssetDto;
import com.example.demo.dto.MediaAssetListResponseDto;
import com.example.demo.service.MediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("media")
@Tag(name = "Media", description = "Media APIs")
public class MediaController {
  private final MediaService mediaService;

  public MediaController(MediaService mediaService) {
    this.mediaService = mediaService;
  }

  @Operation(
      summary = "Get media",
      description = "Returns paginated media assets filtered by kind.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Media assets fetched successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = MediaAssetListResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('media:view')")
  @GetMapping
  public ResponseEntity<MediaAssetListResponseDto> getMedia(
      @Parameter(description = "Page number, starting from 1") @RequestParam(required = false)
          Integer page,
      @Parameter(description = "Page size, default is 20") @RequestParam(required = false)
          Integer pageSize,
      @Parameter(description = "Media kind filter") @RequestParam(required = false) String kind) {
    return ResponseEntity.ok(mediaService.getMedia(page, pageSize, kind));
  }

  @Operation(
      summary = "Upload media",
      description =
          "Uploads a file using multipart form data field name `file` and returns the media asset.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Media uploaded successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = MediaAssetDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid upload request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('media:manage')")
  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MediaAssetDto> uploadMedia(@RequestParam("file") MultipartFile file) {
    return ResponseEntity.status(HttpStatus.CREATED).body(mediaService.uploadMedia(file));
  }

  @Operation(summary = "Delete media", description = "Deletes a media asset by id.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Media deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Media not found")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('media:manage')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMedia(@PathVariable String id) {
    mediaService.deleteMedia(id);
    return ResponseEntity.noContent().build();
  }
}
