package com.example.demo.mapper;

import com.example.demo.dto.AuthorsDto;
import com.example.demo.model.Authors;
import java.util.List;
import java.util.stream.Collectors;

public final class AuthorsMapper {
  private AuthorsMapper() {}

  public static AuthorsDto toDto(Authors authors) {
    if (authors == null) {
      return null;
    }
    AuthorsDto dto = new AuthorsDto();
    dto.setId(authors.getId());
    dto.setDisplayName(authors.getDisplayName());
    dto.setSlug(authors.getSlug());
    dto.setAvatarUrl(authors.getAvatarUrl());
    dto.setBio(toBioDto(authors.getBio()));
    dto.setTitle(authors.getTitle());
    dto.setSocialLinks(toSocialLinkDtos(authors.getSocialLinks()));
    dto.setCreatedAt(authors.getCreatedAt());
    dto.setUpdatedAt(authors.getUpdatedAt());
    return dto;
  }

  public static Authors toModel(AuthorsDto dto) {
    if (dto == null) {
      return null;
    }
    Authors authors = new Authors();
    authors.setDisplayName(dto.getDisplayName());
    authors.setSlug(dto.getSlug());
    authors.setAvatarUrl(dto.getAvatarUrl());
    authors.setBio(toBio(dto.getBio()));
    authors.setTitle(dto.getTitle());
    authors.setSocialLinks(toSocialLinks(dto.getSocialLinks()));
    authors.setCreatedAt(dto.getCreatedAt());
    authors.setUpdatedAt(dto.getUpdatedAt());
    return authors;
  }

  private static AuthorsDto.BioDto toBioDto(Authors.Bio bio) {
    if (bio == null) {
      return null;
    }
    AuthorsDto.BioDto dto = new AuthorsDto.BioDto();
    dto.setEn(bio.getEn());
    dto.setSn(bio.getSn());
    return dto;
  }

  private static Authors.Bio toBio(AuthorsDto.BioDto dto) {
    if (dto == null) {
      return null;
    }
    Authors.Bio bio = new Authors.Bio();
    bio.setEn(dto.getEn());
    bio.setSn(dto.getSn());
    return bio;
  }

  private static List<AuthorsDto.SocialLinkDto> toSocialLinkDtos(
      List<Authors.SocialLink> socialLinks) {
    if (socialLinks == null) {
      return null;
    }
    return socialLinks.stream().map(AuthorsMapper::toSocialLinkDto).collect(Collectors.toList());
  }

  private static List<Authors.SocialLink> toSocialLinks(
      List<AuthorsDto.SocialLinkDto> socialLinks) {
    if (socialLinks == null) {
      return null;
    }
    return socialLinks.stream().map(AuthorsMapper::toSocialLink).collect(Collectors.toList());
  }

  private static AuthorsDto.SocialLinkDto toSocialLinkDto(Authors.SocialLink socialLink) {
    if (socialLink == null) {
      return null;
    }
    AuthorsDto.SocialLinkDto dto = new AuthorsDto.SocialLinkDto();
    dto.setPlatform(socialLink.getPlatform());
    dto.setUrl(socialLink.getUrl());
    return dto;
  }

  private static Authors.SocialLink toSocialLink(AuthorsDto.SocialLinkDto dto) {
    if (dto == null) {
      return null;
    }
    Authors.SocialLink socialLink = new Authors.SocialLink();
    socialLink.setPlatform(dto.getPlatform());
    socialLink.setUrl(dto.getUrl());
    return socialLink;
  }
}
