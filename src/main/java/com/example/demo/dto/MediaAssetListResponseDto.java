package com.example.demo.dto;

import java.util.List;

public class MediaAssetListResponseDto {
  private List<MediaAssetDto> items;
  private long total;
  private int page;
  private int pageSize;

  public List<MediaAssetDto> getItems() {
    return items;
  }

  public void setItems(List<MediaAssetDto> items) {
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
