package com.example.demo.controller;

import com.example.demo.dto.SiteSettingsDto;
import com.example.demo.service.SiteSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("settings")
@Tag(name = "Settings", description = "Site settings APIs")
public class SiteSettingsController {
  private final SiteSettingsService siteSettingsService;

  public SiteSettingsController(SiteSettingsService siteSettingsService) {
    this.siteSettingsService = siteSettingsService;
  }

  @Operation(summary = "Get settings", description = "Returns the site settings or null.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Settings fetched successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = SiteSettingsDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('ADMIN') and hasAuthority('settings:manage')")
  @GetMapping
  public ResponseEntity<SiteSettingsDto> getSettings() {
    return ResponseEntity.ok(siteSettingsService.getSettings());
  }

  @Operation(summary = "Update settings", description = "Creates or updates the site settings.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Settings updated successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = SiteSettingsDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('ADMIN') and hasAuthority('settings:manage')")
  @PutMapping
  public ResponseEntity<SiteSettingsDto> updateSettings(
      @Valid @RequestBody SiteSettingsDto siteSettingsDto) {
    return ResponseEntity.ok(siteSettingsService.upsertSettings(siteSettingsDto));
  }
}
