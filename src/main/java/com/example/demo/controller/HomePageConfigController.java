package com.example.demo.controller;

import com.example.demo.dto.HomePageConfigDto;
import com.example.demo.service.HomePageConfigService;
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
@RequestMapping("homepage-config")
@Tag(name = "Homepage Config", description = "Homepage configuration APIs")
public class HomePageConfigController {
  private final HomePageConfigService homePageConfigService;

  public HomePageConfigController(HomePageConfigService homePageConfigService) {
    this.homePageConfigService = homePageConfigService;
  }

  @Operation(
      summary = "Get homepage config",
      description = "Returns the homepage configuration or null.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Homepage config fetched successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = HomePageConfigDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('homepage:manage')")
  @GetMapping
  public ResponseEntity<HomePageConfigDto> getHomePageConfig() {
    return ResponseEntity.ok(homePageConfigService.getHomePageConfig());
  }

  @Operation(
      summary = "Update homepage config",
      description = "Creates or updates the homepage configuration.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Homepage config updated successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = HomePageConfigDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('homepage:manage')")
  @PutMapping
  public ResponseEntity<HomePageConfigDto> updateHomePageConfig(
      @Valid @RequestBody HomePageConfigDto homePageConfigDto) {
    return ResponseEntity.ok(homePageConfigService.updateHomePageConfig(homePageConfigDto));
  }
}
