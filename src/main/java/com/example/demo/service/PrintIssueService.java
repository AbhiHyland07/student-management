package com.example.demo.service;

import com.example.demo.dto.PrintIssueListResponseDto;
import com.example.demo.dto.PrintIssuesDto;
import com.example.demo.exception.model.BusinessException;
import com.example.demo.exception.model.ResourceNotFound;
import com.example.demo.mapper.PrintIssuesMapper;
import com.example.demo.model.PrintIssues;
import com.example.demo.repository.ArticlesRepository;
import com.example.demo.repository.HomePageConfigRepository;
import com.example.demo.repository.PrintIssuesRepository;
import java.time.Instant;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PrintIssueService {
  private static final int DEFAULT_PAGE_SIZE = 20;

  private final PrintIssuesRepository printIssuesRepository;
  private final ArticlesRepository articlesRepository;
  private final HomePageConfigRepository homePageConfigRepository;

  public PrintIssueService(
      PrintIssuesRepository printIssuesRepository,
      ArticlesRepository articlesRepository,
      HomePageConfigRepository homePageConfigRepository) {
    this.printIssuesRepository = printIssuesRepository;
    this.articlesRepository = articlesRepository;
    this.homePageConfigRepository = homePageConfigRepository;
  }

  public PrintIssueListResponseDto getPrintIssues(Integer page, Integer pageSize) {
    int resolvedPage = page == null || page < 1 ? 1 : page;
    int resolvedPageSize = pageSize == null || pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;

    var pageResult =
        printIssuesRepository.findAllByDeletedAtIsNull(
            PageRequest.of(
                resolvedPage - 1,
                resolvedPageSize,
                Sort.by(Sort.Direction.DESC, "publicationDate")));

    PrintIssueListResponseDto response = new PrintIssueListResponseDto();
    response.setItems(pageResult.getContent().stream().map(PrintIssuesMapper::toDto).toList());
    response.setTotal(response.getItems().size());
    response.setPage(resolvedPage);
    response.setPageSize(resolvedPageSize);
    return response;
  }

  public PrintIssuesDto getLatestPrintIssue() {
    return printIssuesRepository
        .findTopByOrderByPublicationDateDesc(
            PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "publicationDate")))
        .stream()
        .map(PrintIssuesMapper::toDto)
        .findFirst()
        .orElse(null);
  }

  public PrintIssuesDto createPrintIssue(PrintIssuesDto printIssuesDto) {
    PrintIssues printIssue = PrintIssuesMapper.toModel(printIssuesDto);
    printIssue.setId(null);
    Instant now = Instant.now();
    printIssue.setCreatedAt(now);
    printIssue.setUpdatedAt(now);
    printIssue.setDeletedAt(null);
    return PrintIssuesMapper.toDto(printIssuesRepository.save(printIssue));
  }

  public void updatePrintIssue(String id, PrintIssuesDto printIssuesDto) {
    PrintIssues existingPrintIssue =
        printIssuesRepository
            .findByIdAndNotDeleted(id)
            .orElseThrow(() -> new ResourceNotFound("Print issue not found"));
    PrintIssues updatedPrintIssue = PrintIssuesMapper.toModel(printIssuesDto);
    updatedPrintIssue.setId(existingPrintIssue.getId());
    updatedPrintIssue.setCreatedAt(existingPrintIssue.getCreatedAt());
    updatedPrintIssue.setUpdatedAt(Instant.now());
    updatedPrintIssue.setDeletedAt(existingPrintIssue.getDeletedAt());
    printIssuesRepository.save(updatedPrintIssue);
  }

  public void deletePrintIssue(String id) {
    PrintIssues printIssue =
        printIssuesRepository
            .findByIdAndNotDeleted(id)
            .orElseThrow(() -> new ResourceNotFound("Print issue not found"));

    // Check referential integrity
    long articlesCount = articlesRepository.countByPrintIssueIdAndNotDeleted(id);
    long homeConfigCount = homePageConfigRepository.countByFeaturePrintIssueIdAndNotDeleted(id);

    if (articlesCount > 0 || homeConfigCount > 0) {
      throw new BusinessException(
          "Cannot delete print issue: "
              + articlesCount
              + " articles and "
              + homeConfigCount
              + " home page configs reference this print issue");
    }

    // Soft delete
    printIssue.setDeletedAt(Instant.now());
    printIssuesRepository.save(printIssue);
  }
}
