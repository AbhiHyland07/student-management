package com.example.demo.mapper;

import com.example.demo.dto.UsersDto;
import com.example.demo.model.Users;

public final class UsersMapper {
  private UsersMapper() {}

  public static UsersDto toDto(Users users) {
    if (users == null) {
      return null;
    }
    UsersDto dto = new UsersDto();
    dto.setId(users.getId());
    dto.setFullName(users.getFullName());
    dto.setEmail(users.getEmail());
    dto.setRole(users.getRole());
    dto.setAuthorId(users.getAuthorId());
    dto.setAvatarUrl(users.getAvatarUrl());
    dto.setStatus(users.getStatus());
    dto.setPreferredLanguage(users.getPreferredLanguage());
    dto.setPermissions(users.getPermissions());
    dto.setLastLoginAt(users.getLastLoginAt());
    dto.setCreatedAt(users.getCreatedAt());
    dto.setUpdatedAt(users.getUpdatedAt());
    return dto;
  }

  public static Users toModel(UsersDto dto) {
    if (dto == null) {
      return null;
    }
    Users users = new Users();
    users.setFullName(dto.getFullName());
    users.setEmail(dto.getEmail());
    users.setRole(dto.getRole());
    users.setAuthorId(dto.getAuthorId());
    users.setAvatarUrl(dto.getAvatarUrl());
    users.setStatus(dto.getStatus());
    users.setPreferredLanguage(dto.getPreferredLanguage());
    users.setPermissions(dto.getPermissions());
    users.setLastLoginAt(dto.getLastLoginAt());
    users.setCreatedAt(dto.getCreatedAt());
    users.setUpdatedAt(dto.getUpdatedAt());
    return users;
  }
}
