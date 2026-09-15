package com.example.demo.mapper;

import com.example.demo.dto.SiteSettingsDto;
import com.example.demo.model.SiteSettings;

public final class SiteSettingsMapper {
  private SiteSettingsMapper() {}

  public static SiteSettingsDto toDto(SiteSettings siteSettings) {
    if (siteSettings == null) {
      return null;
    }
    SiteSettingsDto dto = new SiteSettingsDto();
    dto.setId(siteSettings.getId());
    dto.setSiteName(toSiteNameDto(siteSettings.getSiteName()));
    dto.setContactEmail(siteSettings.getContactEmail());
    dto.setDefaultShareImageUrl(siteSettings.getDefaultShareImageUrl());
    dto.setUpdatedAt(siteSettings.getUpdatedAt());
    return dto;
  }

  public static SiteSettings toModel(SiteSettingsDto dto) {
    if (dto == null) {
      return null;
    }
    SiteSettings siteSettings = new SiteSettings();
    siteSettings.setSiteName(toSiteName(dto.getSiteName()));
    siteSettings.setContactEmail(dto.getContactEmail());
    siteSettings.setDefaultShareImageUrl(dto.getDefaultShareImageUrl());
    siteSettings.setUpdatedAt(dto.getUpdatedAt());
    return siteSettings;
  }

  private static SiteSettingsDto.SiteNameDto toSiteNameDto(SiteSettings.SiteName siteName) {
    if (siteName == null) {
      return null;
    }
    SiteSettingsDto.SiteNameDto dto = new SiteSettingsDto.SiteNameDto();
    dto.setEnglish(siteName.getEnglish());
    dto.setBengali(siteName.getBengali());
    return dto;
  }

  private static SiteSettings.SiteName toSiteName(SiteSettingsDto.SiteNameDto dto) {
    if (dto == null) {
      return null;
    }
    SiteSettings.SiteName siteName = new SiteSettings.SiteName();
    siteName.setEnglish(dto.getEnglish());
    siteName.setBengali(dto.getBengali());
    return siteName;
  }
}
