package com.example.demo.mapper;

import com.example.demo.dto.SeoDto;
import com.example.demo.model.Seo;

public final class SeoMapper {
  private SeoMapper() {}

  public static SeoDto toDto(Seo seo) {
    if (seo == null) {
      return null;
    }
    SeoDto dto = new SeoDto();
    dto.setEn(toSeoDetailsDto(seo.getEn()));
    dto.setBn(toSeoDetailsDto(seo.getBn()));
    return dto;
  }

  public static Seo toModel(SeoDto dto) {
    if (dto == null) {
      return null;
    }
    Seo seo = new Seo();
    seo.setEn(toSeoDetails(dto.getEn()));
    seo.setBn(toSeoDetails(dto.getBn()));
    return seo;
  }

  private static SeoDto.SeoDetailsDto toSeoDetailsDto(Seo.SeoDetails seoDetails) {
    if (seoDetails == null) {
      return null;
    }
    SeoDto.SeoDetailsDto dto = new SeoDto.SeoDetailsDto();
    dto.setMetaTitle(seoDetails.getMetaTitle());
    dto.setMetaDescription(seoDetails.getMetaDescription());
    dto.setFocusKeywords(seoDetails.getFocusKeywords());
    dto.setCanonicalUrl(seoDetails.getCanonicalUrl());
    dto.setOgImageUrl(seoDetails.getOgImageUrl());
    dto.setRobotIndex(seoDetails.getRobotIndex());
    dto.setRobotFollow(seoDetails.getRobotFollow());
    dto.setSchemaType(seoDetails.getSchemaType());
    dto.setKeyTakeaway(seoDetails.getKeyTakeaway());
    return dto;
  }

  private static Seo.SeoDetails toSeoDetails(SeoDto.SeoDetailsDto dto) {
    if (dto == null) {
      return null;
    }
    Seo.SeoDetails seoDetails = new Seo.SeoDetails();
    seoDetails.setMetaTitle(dto.getMetaTitle());
    seoDetails.setMetaDescription(dto.getMetaDescription());
    seoDetails.setFocusKeywords(dto.getFocusKeywords());
    seoDetails.setCanonicalUrl(dto.getCanonicalUrl());
    seoDetails.setOgImageUrl(dto.getOgImageUrl());
    seoDetails.setRobotIndex(dto.getRobotIndex());
    seoDetails.setRobotFollow(dto.getRobotFollow());
    seoDetails.setSchemaType(dto.getSchemaType());
    seoDetails.setKeyTakeaway(dto.getKeyTakeaway());
    return seoDetails;
  }
}
