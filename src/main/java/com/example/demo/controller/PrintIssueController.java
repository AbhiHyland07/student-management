package com.example.demo.controller;

import com.example.demo.dto.PrintIssueListResponseDto;
import com.example.demo.dto.PrintIssuesDto;
import com.example.demo.service.PrintIssueService;
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
@RequestMapping("print-issues")
@Tag(name = "Print Issues", description = "Print issue APIs")
public class PrintIssueController {
  private final PrintIssueService printIssueService;

  public PrintIssueController(PrintIssueService printIssueService) {
    this.printIssueService = printIssueService;
  }

  @Operation(
      summary = "Get print issues",
      description = "Returns paginated print issues with default page size 20.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Print issues fetched successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PrintIssueListResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('print_issue:view')")
  @GetMapping
  public ResponseEntity<PrintIssueListResponseDto> getPrintIssues(
      @Parameter(description = "Page number, starting from 1") @RequestParam(required = false)
          Integer page,
      @Parameter(description = "Page size, default is 20") @RequestParam(required = false)
          Integer pageSize) {
    return ResponseEntity.ok(printIssueService.getPrintIssues(page, pageSize));
  }

  @Operation(
      summary = "Get latest print issue",
      description = "Returns the latest print issue or null.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Latest print issue fetched successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PrintIssuesDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('print_issue:view')")
  @GetMapping("/latest")
  public ResponseEntity<PrintIssuesDto> getLatestPrintIssue() {
    return ResponseEntity.ok(printIssueService.getLatestPrintIssue());
  }

  @Operation(summary = "Create print issue", description = "Creates a new print issue.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Print issue created successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PrintIssuesDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('print_issue:manage')")
  @PostMapping
  public ResponseEntity<PrintIssuesDto> createPrintIssue(
      @Valid @RequestBody PrintIssuesDto printIssuesDto) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(printIssueService.createPrintIssue(printIssuesDto));
  }

  @Operation(summary = "Update print issue", description = "Updates an existing print issue.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Print issue updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Print issue not found")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('print_issue:manage')")
  @PutMapping("/{id}")
  public ResponseEntity<Void> updatePrintIssue(
      @PathVariable String id, @Valid @RequestBody PrintIssuesDto printIssuesDto) {
    printIssueService.updatePrintIssue(id, printIssuesDto);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Delete print issue", description = "Deletes a print issue by id.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Print issue deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Print issue not found")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('print_issue:manage')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePrintIssue(@PathVariable String id) {
    printIssueService.deletePrintIssue(id);
    return ResponseEntity.noContent().build();
  }
}
