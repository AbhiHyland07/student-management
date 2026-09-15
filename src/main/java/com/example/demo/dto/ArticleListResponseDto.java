package com.example.demo.dto;

import java.util.List;

public class ArticleListResponseDto {
  private List<ArticlesDto> items;
  private long total;
  private int pageSize;
  private int page;

  public List<ArticlesDto> getItems() {
    return items;
  }

  public void setItems(List<ArticlesDto> items) {
    this.items = items;
  }

  public long getTotal() {
    return total;
  }

  public void setTotal(long total) {
    this.total = total;
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
