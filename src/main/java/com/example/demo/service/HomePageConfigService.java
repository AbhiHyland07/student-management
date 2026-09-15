package com.example.demo.service;

import com.example.demo.dto.HomePageConfigDto;
import com.example.demo.mapper.HomePageConfigMapper;
import com.example.demo.model.HomePageConfig;
import com.example.demo.repository.HomePageConfigRepository;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class HomePageConfigService {
  private final HomePageConfigRepository homePageConfigRepository;

  public HomePageConfigService(HomePageConfigRepository homePageConfigRepository) {
    this.homePageConfigRepository = homePageConfigRepository;
  }

  public HomePageConfigDto getHomePageConfig() {
    return homePageConfigRepository.findAll().stream()
        .findFirst()
        .map(HomePageConfigMapper::toDto)
        .orElse(null);
  }

  public HomePageConfigDto updateHomePageConfig(HomePageConfigDto homePageConfigDto) {
    HomePageConfig existingConfig =
        homePageConfigRepository.findAll().stream().findFirst().orElse(null);
    HomePageConfig config = HomePageConfigMapper.toModel(homePageConfigDto);
    if (existingConfig != null) {
      config.setId(existingConfig.getId());
    }
    config.setUpdatedAt(Instant.now());
    return HomePageConfigMapper.toDto(homePageConfigRepository.save(config));
  }
}
