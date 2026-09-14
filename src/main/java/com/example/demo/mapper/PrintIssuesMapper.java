package com.example.demo.mapper;

import com.example.demo.dto.PrintIssuesDto;
import com.example.demo.model.PrintIssues;

public final class PrintIssuesMapper {
  private PrintIssuesMapper() {}

  public static PrintIssuesDto toDto(PrintIssues printIssues) {
    if (printIssues == null) {
      return null;
    }
    PrintIssuesDto dto = new PrintIssuesDto();
    dto.setId(printIssues.getId());
    dto.setVolumeNumber(printIssues.getVolumeNumber());
    dto.setIssueNumber(printIssues.getIssueNumber());
    dto.setPublicationDate(printIssues.getPublicationDate());
    dto.setCoverImageUrl(printIssues.getCoverImageUrl());
    dto.setPdfUrl(printIssues.getPdfUrl());
    dto.setPdfFileSizeBytes(printIssues.getPdfFileSizeBytes());
    dto.setIsPublished(printIssues.getIsPublished());
    dto.setCreatedAt(printIssues.getCreatedAt());
    dto.setUpdatedAt(printIssues.getUpdatedAt());
    return dto;
  }

  public static PrintIssues toModel(PrintIssuesDto dto) {
    if (dto == null) {
      return null;
    }
    PrintIssues printIssues = new PrintIssues();
    printIssues.setId(dto.getId());
    printIssues.setVolumeNumber(dto.getVolumeNumber());
    printIssues.setIssueNumber(dto.getIssueNumber());
    printIssues.setPublicationDate(dto.getPublicationDate());
    printIssues.setCoverImageUrl(dto.getCoverImageUrl());
    printIssues.setPdfUrl(dto.getPdfUrl());
    printIssues.setPdfFileSizeBytes(dto.getPdfFileSizeBytes());
    printIssues.setIsPublished(dto.getIsPublished());
    printIssues.setCreatedAt(dto.getCreatedAt());
    printIssues.setUpdatedAt(dto.getUpdatedAt());
    return printIssues;
  }
}
