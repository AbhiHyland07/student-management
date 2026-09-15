package com.example.demo.controller;

import com.example.demo.dto.ArticleListResponseDto;
import com.example.demo.dto.ArticleSummaryDto;
import com.example.demo.dto.ArticlesDto;
import com.example.demo.model.enums.ArticleStatus;
import com.example.demo.model.enums.ArticleType;
import com.example.demo.model.enums.PublicationSource;
import com.example.demo.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("articles")
@Tag(name = "Articles", description = "Article listing APIs")
public class ArticleController {
  private final ArticleService articleService;

  public ArticleController(ArticleService articleService) {
    this.articleService = articleService;
  }

  @Operation(
      summary = "Get articles",
      description =
          "Returns paginated articles filtered by search, publication source, article type, status,"
              + " author, print issue, and video presence.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Articles fetched successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ArticleListResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('article:view')")
  @GetMapping
  public ResponseEntity<ArticleListResponseDto> getArticles(
      @Parameter(description = "Page number, starting from 1") @RequestParam(required = false)
          Integer page,
      @Parameter(description = "Page size, default is 20") @RequestParam(required = false)
          Integer pageSize,
      @Parameter(description = "Search text against article title") @RequestParam(required = false)
          String search,
      @Parameter(description = "Publication source") @RequestParam(required = false)
          PublicationSource publicationSource,
      @Parameter(description = "Article type") @RequestParam(required = false)
          ArticleType articleType,
      @Parameter(description = "Article status") @RequestParam(required = false)
          ArticleStatus status,
      @Parameter(description = "Author id") @RequestParam(required = false) String authorId,
      @Parameter(description = "Print issue id") @RequestParam(required = false)
          String printIssueId,
      @Parameter(description = "Whether article has video") @RequestParam(required = false)
          Boolean hasVideo,
      @Parameter(description = "Sort field: createdAt, updatedAt, publicationDate, publishedAt")
          @RequestParam(required = false)
          String sortBy,
      @Parameter(description = "Sort direction: asc or desc") @RequestParam(required = false)
          String sortDirection) {
    return ResponseEntity.ok(
        articleService.getArticles(
            page,
            pageSize,
            search,
            publicationSource,
            articleType,
            status,
            authorId,
            printIssueId,
            hasVideo,
            sortBy,
            sortDirection));
  }

  @Operation(
      summary = "Get articles by ids",
      description = "Returns articles matching the given ids.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Articles fetched successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ArticleSummaryDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('article:view')")
  @GetMapping("/by-ids")
  public ResponseEntity<List<ArticleSummaryDto>> getArticlesByIds(
      @Parameter(description = "Comma-separated article ids") @RequestParam String ids) {
    return ResponseEntity.ok(articleService.getArticlesByIds(ids));
  }

  @Operation(summary = "Get article by id", description = "Returns the full article by id.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Article fetched successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ArticlesDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Article not found")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('article:view')")
  @GetMapping("/{id}")
  public ResponseEntity<ArticlesDto> getArticleById(
      @Parameter(description = "Article id") @PathVariable String id) {
    return ResponseEntity.ok(articleService.getArticleById(id));
  }

  @Operation(
      summary = "Create article",
      description = "Creates a new article and returns the article summary response.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Article created successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ArticleSummaryDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('article:create')")
  @PostMapping
  public ResponseEntity<ArticleSummaryDto> createArticle(@Valid @RequestBody ArticlesDto article) {
    return ResponseEntity.status(HttpStatus.CREATED).body(articleService.createArticle(article));
  }

  @Operation(summary = "Update article", description = "Updates an existing article.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Article updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Article not found")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('article:edit')")
  @PutMapping("/{id}")
  public ResponseEntity<Void> updateArticle(
      @PathVariable String id, @Valid @RequestBody ArticlesDto article) {
    articleService.updateArticle(id, article);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Delete article", description = "Deletes an article by id.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Article deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Article not found")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('article:delete')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteArticle(@PathVariable String id) {
    articleService.deleteArticle(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "Duplicate article",
      description =
          "Duplicates an existing article, saves the copy as DRAFT, and returns the full article.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Article duplicated successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ArticlesDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Article not found")
      })
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("hasAuthority('article:create')")
  @PostMapping("/{id}/duplication")
  public ResponseEntity<ArticlesDto> duplicateArticle(@PathVariable String id) {
    return ResponseEntity.ok(articleService.duplicateArticle(id));
  }
}
