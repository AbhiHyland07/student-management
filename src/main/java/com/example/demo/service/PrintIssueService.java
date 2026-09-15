package com.example.demo.service;

import com.example.demo.dto.PrintIssueListResponseDto;
import com.example.demo.dto.PrintIssuesDto;
import com.example.demo.exception.model.ResourceNotFound;
import com.example.demo.mapper.PrintIssuesMapper;
import com.example.demo.model.PrintIssues;
import com.example.demo.repository.PrintIssuesRepository;
import java.time.Instant;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PrintIssueService {
  private static final int DEFAULT_PAGE_SIZE = 20;

  private final PrintIssuesRepository printIssuesRepository;

  public PrintIssueService(PrintIssuesRepository printIssuesRepository) {
    this.printIssuesRepository = printIssuesRepository;
  }

  public PrintIssueListResponseDto getPrintIssues(Integer page, Integer pageSize) {
    int resolvedPage = page == null || page < 1 ? 1 : page;
    int resolvedPageSize = pageSize == null || pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;

    var pageResult =
        printIssuesRepository.findAll(
            PageRequest.of(
                resolvedPage - 1,
                resolvedPageSize,
                Sort.by(Sort.Direction.DESC, "publicationDate")));

    PrintIssueListResponseDto response = new PrintIssueListResponseDto();
    response.setItems(pageResult.getContent().stream().map(PrintIssuesMapper::toDto).toList());
    response.setTotal(printIssuesRepository.count());
    response.setPage(resolvedPage);
    response.setPageSize(resolvedPageSize);
    return response;
  }

  public PrintIssuesDto getLatestPrintIssue() {
    return printIssuesRepository
        .findTopByOrderByPublicationDateDesc()
        .map(PrintIssuesMapper::toDto)
        .orElse(null);
  }

  public PrintIssuesDto createPrintIssue(PrintIssuesDto printIssuesDto) {
    PrintIssues printIssue = PrintIssuesMapper.toModel(printIssuesDto);
    Instant now = Instant.now();
    printIssue.setCreatedAt(now);
    printIssue.setUpdatedAt(now);
    return PrintIssuesMapper.toDto(printIssuesRepository.save(printIssue));
  }

  public void updatePrintIssue(String id, PrintIssuesDto printIssuesDto) {
    PrintIssues existingPrintIssue =
        printIssuesRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFound("Print issue not found"));
    PrintIssues updatedPrintIssue = PrintIssuesMapper.toModel(printIssuesDto);
    updatedPrintIssue.setId(existingPrintIssue.getId());
    updatedPrintIssue.setCreatedAt(existingPrintIssue.getCreatedAt());
    updatedPrintIssue.setUpdatedAt(Instant.now());
    printIssuesRepository.save(updatedPrintIssue);
  }

  public void deletePrintIssue(String id) {
    PrintIssues existingPrintIssue =
        printIssuesRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFound("Print issue not found"));
    printIssuesRepository.delete(existingPrintIssue);
  }
}
