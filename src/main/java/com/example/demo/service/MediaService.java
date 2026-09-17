package com.example.demo.service;

import com.example.demo.dto.MediaAssetDto;
import com.example.demo.dto.MediaAssetListResponseDto;
import com.example.demo.exception.model.FileUploadException;
import com.example.demo.exception.model.InvalidRequestException;
import com.example.demo.exception.model.ResourceNotFound;
import com.example.demo.mapper.MediaAssetMapper;
import com.example.demo.model.MediaAsset;
import com.example.demo.model.authentication.UserExtend;
import com.example.demo.repository.MediaAssetRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MediaService {
  private static final int DEFAULT_PAGE_SIZE = 20;
  private static final Path UPLOAD_DIRECTORY = Path.of("uploads");

  private final MediaAssetRepository mediaAssetRepository;
  private final MongoTemplate mongoTemplate;

  public MediaService(MediaAssetRepository mediaAssetRepository, MongoTemplate mongoTemplate) {
    this.mediaAssetRepository = mediaAssetRepository;
    this.mongoTemplate = mongoTemplate;
  }

  public MediaAssetListResponseDto getMedia(Integer page, Integer pageSize, String kind) {
    int resolvedPage = page == null || page < 1 ? 1 : page;
    int resolvedPageSize = pageSize == null || pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;

    Query query = new Query();
    query.addCriteria(Criteria.where("deletedAt").is(null));
    if (kind != null && !kind.isBlank()) {
      query.addCriteria(Criteria.where("kind").is(kind));
    }

    query.with(Sort.by(Sort.Direction.DESC, "createdAt"));
    query.skip((long) (resolvedPage - 1) * resolvedPageSize);
    query.limit(resolvedPageSize);

    MediaAssetListResponseDto response = new MediaAssetListResponseDto();
    response.setItems(
        mongoTemplate.find(query, MediaAsset.class).stream().map(MediaAssetMapper::toDto).toList());
    response.setTotal(response.getItems().size());
    response.setPage(resolvedPage);
    response.setPageSize(resolvedPageSize);
    return response;
  }

  public MediaAssetDto uploadMedia(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new InvalidRequestException("File is required");
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null
        || !(authentication.getPrincipal() instanceof UserExtend userExtend)) {
      throw new InvalidRequestException("User must be authenticated to upload media");
    }

    String currentUserId = userExtend.getUserDocument().getId();

    try {
      Files.createDirectories(UPLOAD_DIRECTORY);
      String originalFileName =
          file.getOriginalFilename() == null ? "file" : file.getOriginalFilename();
      String storedFileName = UUID.randomUUID() + "-" + originalFileName;
      Path targetPath = UPLOAD_DIRECTORY.resolve(storedFileName);
      Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

      MediaAsset mediaAsset = new MediaAsset();
      mediaAsset.setUrl("/uploads/" + storedFileName);
      mediaAsset.setFileName(originalFileName);
      mediaAsset.setKind(resolveKind(file.getContentType()));
      mediaAsset.setMimeType(file.getContentType());
      mediaAsset.setSizeBytes(file.getSize());
      mediaAsset.setUploadedBy(currentUserId);
      mediaAsset.setCreatedAt(Instant.now());
      mediaAsset.setDeletedAt(null);

      return MediaAssetMapper.toDto(mediaAssetRepository.save(mediaAsset));
    } catch (IOException e) {
      throw new FileUploadException("Failed to upload file", e);
    }
  }

  public void deleteMedia(String id) {
    MediaAsset mediaAsset =
        mediaAssetRepository
            .findByIdAndNotDeleted(id)
            .orElseThrow(() -> new ResourceNotFound("Media not found"));
    mediaAsset.setDeletedAt(Instant.now());
    mediaAssetRepository.save(mediaAsset);
  }

  private String resolveKind(String contentType) {
    if (contentType == null || contentType.isBlank()) {
      return "OTHER";
    }
    if (contentType.startsWith("image/")) {
      return "IMAGE";
    }
    if (contentType.startsWith("video/")) {
      return "VIDEO";
    }
    if ("application/pdf".equals(contentType)) {
      return "PDF";
    }
    return "OTHER";
  }
}
