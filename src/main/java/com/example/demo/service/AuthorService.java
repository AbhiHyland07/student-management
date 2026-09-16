package com.example.demo.service;

import com.example.demo.dto.AuthorListResponseDto;
import com.example.demo.dto.AuthorsDto;
import com.example.demo.exception.model.BusinessException;
import com.example.demo.exception.model.ResourceNotFound;
import com.example.demo.mapper.AuthorsMapper;
import com.example.demo.model.Authors;
import com.example.demo.repository.ArticlesRepository;
import com.example.demo.repository.AuthorsRepository;
import com.example.demo.repository.UsersRepository;
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
  private final ArticlesRepository articlesRepository;
  private final UsersRepository usersRepository;

  public AuthorService(
      MongoTemplate mongoTemplate,
      AuthorsRepository authorsRepository,
      ArticlesRepository articlesRepository,
      UsersRepository usersRepository) {
    this.mongoTemplate = mongoTemplate;
    this.authorsRepository = authorsRepository;
    this.articlesRepository = articlesRepository;
    this.usersRepository = usersRepository;
  }

  public AuthorListResponseDto getAuthors(Integer page, Integer pageSize, String search) {
    int resolvedPage = page == null || page < 1 ? 1 : page;
    int resolvedPageSize = pageSize == null || pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;

    Query query = new Query();
    query.addCriteria(Criteria.where("deletedAt").is(null));
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

    query.with(Sort.by(Sort.Direction.DESC, "createdAt"));
    query.skip((long) (resolvedPage - 1) * resolvedPageSize);
    query.limit(resolvedPageSize);

    AuthorListResponseDto response = new AuthorListResponseDto();
    response.setItems(
        mongoTemplate.find(query, Authors.class).stream().map(AuthorsMapper::toDto).toList());
    response.setTotalCount(response.getItems().size());
    response.setPage(resolvedPage);
    response.setPageSize(resolvedPageSize);
    return response;
  }

  public AuthorsDto createAuthor(AuthorsDto authorsDto) {
    Authors author = AuthorsMapper.toModel(authorsDto);
    author.setId(null);
    Instant now = Instant.now();
    author.setCreatedAt(now);
    author.setUpdatedAt(now);
    author.setDeletedAt(null);
    return AuthorsMapper.toDto(authorsRepository.save(author));
  }

  public void updateAuthor(String id, AuthorsDto authorsDto) {
    Authors existingAuthor =
        authorsRepository
            .findByIdAndNotDeleted(id)
            .orElseThrow(() -> new ResourceNotFound("Author not found"));
    Authors updatedAuthor = AuthorsMapper.toModel(authorsDto);
    updatedAuthor.setId(existingAuthor.getId());
    updatedAuthor.setCreatedAt(existingAuthor.getCreatedAt());
    updatedAuthor.setUpdatedAt(Instant.now());
    updatedAuthor.setDeletedAt(existingAuthor.getDeletedAt());
    authorsRepository.save(updatedAuthor);
  }

  public void deleteAuthor(String id) {
    Authors author =
        authorsRepository
            .findByIdAndNotDeleted(id)
            .orElseThrow(() -> new ResourceNotFound("Author not found"));

    // Check referential integrity
    long articlesCount = articlesRepository.countByAuthorIdAndNotDeleted(id);
    long usersCount = usersRepository.countByAuthorIdAndNotDeleted(id);

    if (articlesCount > 0 || usersCount > 0) {
      throw new BusinessException(
          "Cannot delete author: "
              + articlesCount
              + " articles and "
              + usersCount
              + " users reference this author");
    }

    // Soft delete
    author.setDeletedAt(Instant.now());
    authorsRepository.save(author);
  }
}
