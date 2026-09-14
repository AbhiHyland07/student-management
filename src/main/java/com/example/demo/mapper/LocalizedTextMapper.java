package com.example.demo.mapper;

import com.example.demo.dto.LocalizedTextDto;
import com.example.demo.model.LocalizedText;

public final class LocalizedTextMapper {
  private LocalizedTextMapper() {}

  public static LocalizedTextDto toDto(LocalizedText localizedText) {
    if (localizedText == null) {
      return null;
    }
    LocalizedTextDto dto = new LocalizedTextDto();
    dto.setEn(localizedText.getEn());
    dto.setBn(localizedText.getBn());
    return dto;
  }

  public static LocalizedText toModel(LocalizedTextDto dto) {
    if (dto == null) {
      return null;
    }
    LocalizedText localizedText = new LocalizedText();
    localizedText.setEn(dto.getEn());
    localizedText.setBn(dto.getBn());
    return localizedText;
  }
}
