package com.example.demo.mapper;

import com.example.demo.dto.MediaAssetDto;
import com.example.demo.model.MediaAsset;

public final class MediaAssetMapper {
  private MediaAssetMapper() {}

  public static MediaAssetDto toDto(MediaAsset mediaAsset) {
    if (mediaAsset == null) {
      return null;
    }
    MediaAssetDto dto = new MediaAssetDto();
    dto.setId(mediaAsset.getId());
    dto.setUrl(mediaAsset.getUrl());
    dto.setFileName(mediaAsset.getFileName());
    dto.setKind(mediaAsset.getKind());
    dto.setMimeType(mediaAsset.getMimeType());
    dto.setSizeBytes(mediaAsset.getSizeBytes());
    dto.setAltText(mediaAsset.getAltText());
    dto.setUploadedBy(mediaAsset.getUploadedBy());
    dto.setCreatedAt(mediaAsset.getCreatedAt());
    return dto;
  }

  public static MediaAsset toModel(MediaAssetDto dto) {
    if (dto == null) {
      return null;
    }
    MediaAsset mediaAsset = new MediaAsset();
    mediaAsset.setId(dto.getId());
    mediaAsset.setUrl(dto.getUrl());
    mediaAsset.setFileName(dto.getFileName());
    mediaAsset.setKind(dto.getKind());
    mediaAsset.setMimeType(dto.getMimeType());
    mediaAsset.setSizeBytes(dto.getSizeBytes());
    mediaAsset.setAltText(dto.getAltText());
    mediaAsset.setUploadedBy(dto.getUploadedBy());
    mediaAsset.setCreatedAt(dto.getCreatedAt());
    return mediaAsset;
  }
}
