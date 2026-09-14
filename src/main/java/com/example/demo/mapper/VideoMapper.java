package com.example.demo.mapper;

import com.example.demo.dto.VideoDto;
import com.example.demo.model.Video;

public final class VideoMapper {
  private VideoMapper() {}

  public static VideoDto toDto(Video video) {
    if (video == null) {
      return null;
    }
    VideoDto dto = new VideoDto();
    dto.setProvider(video.getProvider());
    dto.setUrl(video.getUrl());
    dto.setEmbeddedUrl(video.getEmbeddedUrl());
    dto.setThumbnailUrl(video.getThumbnailUrl());
    return dto;
  }

  public static Video toModel(VideoDto dto) {
    if (dto == null) {
      return null;
    }
    Video video = new Video();
    video.setProvider(dto.getProvider());
    video.setUrl(dto.getUrl());
    video.setEmbeddedUrl(dto.getEmbeddedUrl());
    video.setThumbnailUrl(dto.getThumbnailUrl());
    return video;
  }
}
