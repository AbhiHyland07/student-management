package com.example.demo.service;

import com.example.demo.dto.AuthorListResponseDto;
import com.example.demo.dto.AuthorsDto;
import com.example.demo.exception.model.ResourceNotFound;
import com.example.demo.mapper.AuthorsMapper;
import com.example.demo.model.Authors;
import com.example.demo.repository.AuthorsRepository;
import java.time.Instant;
import java.util.regex.Pattern;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
  private static final int DEFAULT_PAGE_SIZE = 20;

  private final MongoTemplate mongoTemplate;
  private final AuthorsRepository authorsRepository;

  public AuthorService(MongoTemplate mongoTemplate, AuthorsRepository authorsRepository) {
    this.mongoTemplate = mongoTemplate;
    this.authorsRepository = authorsRepository;
  }

  public AuthorListResponseDto getAuthors(Integer page, Integer pageSize, String search) {
    int resolvedPage = page == null || page < 1 ? 1 : page;
    int resolvedPageSize = pageSize == null || pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;

    Query query = new Query();
    if (search != null && !search.isBlank()) {
      String escapedSearch = Pattern.quote(search.trim());
      query.addCriteria(
          new Criteria()
              .orOperator(
                  Criteria.where("displayName").regex(escapedSearch, "i"),
                  Criteria.where("slug").regex(escapedSearch, "i"),
                  Criteria.where("title").regex(escapedSearch, "i"),
                  Criteria.where("bio.en").regex(escapedSearch, "i"),
                  Criteria.where("bio.sn").regex(escapedSearch, "i")));
    }

    long totalCount = mongoTemplate.count(new Query(), Authors.class);

    query.with(Sort.by(Sort.Direction.DESC, "createdAt"));
    query.skip((long) (resolvedPage - 1) * resolvedPageSize);
    query.limit(resolvedPageSize);

    AuthorListResponseDto response = new AuthorListResponseDto();
    response.setItems(
        mongoTemplate.find(query, Authors.class).stream().map(AuthorsMapper::toDto).toList());
    response.setTotalCount(totalCount);
    response.setPage(resolvedPage);
    response.setPageSize(resolvedPageSize);
    return response;
  }

  public AuthorsDto createAuthor(AuthorsDto authorsDto) {
    Authors author = AuthorsMapper.toModel(authorsDto);
    Instant now = Instant.now();
    author.setCreatedAt(now);
    author.setUpdatedAt(now);
    return AuthorsMapper.toDto(authorsRepository.save(author));
  }

  public void updateAuthor(String id, AuthorsDto authorsDto) {
    Authors existingAuthor =
        authorsRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Author not found"));
    Authors updatedAuthor = AuthorsMapper.toModel(authorsDto);
    updatedAuthor.setId(existingAuthor.getId());
    updatedAuthor.setCreatedAt(existingAuthor.getCreatedAt());
    updatedAuthor.setUpdatedAt(Instant.now());
    authorsRepository.save(updatedAuthor);
  }

  public void deleteAuthor(String id) {
    Authors existingAuthor =
        authorsRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Author not found"));
    authorsRepository.delete(existingAuthor);
  }
}
