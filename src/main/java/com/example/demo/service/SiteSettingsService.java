package com.example.demo.service;

import com.example.demo.dto.SiteSettingsDto;
import com.example.demo.mapper.SiteSettingsMapper;
import com.example.demo.model.SiteSettings;
import com.example.demo.repository.SiteSettingsRepository;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class SiteSettingsService {
  private final SiteSettingsRepository siteSettingsRepository;

  public SiteSettingsService(SiteSettingsRepository siteSettingsRepository) {
    this.siteSettingsRepository = siteSettingsRepository;
  }

  public SiteSettingsDto getSettings() {
    return siteSettingsRepository.findAllByDeletedAtIsNull().stream()
        .findFirst()
        .map(SiteSettingsMapper::toDto)
        .orElse(null);
  }

  public SiteSettingsDto upsertSettings(SiteSettingsDto siteSettingsDto) {
    SiteSettings existingSettings =
        siteSettingsRepository.findAllByDeletedAtIsNull().stream().findFirst().orElse(null);
    SiteSettings siteSettings = SiteSettingsMapper.toModel(siteSettingsDto);
    if (existingSettings != null) {
      siteSettings.setId(existingSettings.getId());
    } else {
      siteSettings.setId(null);
      siteSettings.setDeletedAt(null);
    }
    siteSettings.setUpdatedAt(Instant.now());
    return SiteSettingsMapper.toDto(siteSettingsRepository.save(siteSettings));
  }
}
