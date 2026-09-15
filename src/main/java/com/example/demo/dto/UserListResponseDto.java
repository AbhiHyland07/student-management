package com.example.demo.dto;

import java.util.List;

public class UserListResponseDto {
  private List<UsersDto> items;
  private long total;
  private int page;
  private int pageSize;

  public List<UsersDto> getItems() {
    return items;
  }

  public void setItems(List<UsersDto> items) {
    this.items = items;
  }

  public long getTotal() {
    return total;
  }

  public void setTotal(long total) {
    this.total = total;
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }
}
