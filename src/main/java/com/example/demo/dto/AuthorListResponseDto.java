package com.example.demo.dto;

import java.util.List;

public class AuthorListResponseDto {
  private List<AuthorsDto> items;
  private long totalCount;
  private int pageSize;
  private int page;

  public List<AuthorsDto> getItems() {
    return items;
  }

  public void setItems(List<AuthorsDto> items) {
    this.items = items;
  }

  public long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(long totalCount) {
    this.totalCount = totalCount;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }
}
