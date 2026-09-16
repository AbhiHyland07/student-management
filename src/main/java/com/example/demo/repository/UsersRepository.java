package com.example.demo.repository;

import com.example.demo.model.Users;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UsersRepository extends MongoRepository<Users, String> {
  @Query("{ 'email': ?0, 'deletedAt': null }")
  Optional<Users> findByEmail(String email);

  @Query("{ 'passwordResetToken': ?0, 'deletedAt': null }")
  Optional<Users> findByPasswordResetToken(String passwordResetToken);

  @Query("{ '_id': ?0, 'deletedAt': null }")
  Optional<Users> findByIdAndNotDeleted(String id);

  @Query(value = "{ 'deletedAt': null }", count = true)
  Long countAll();

  @Query("{ 'deletedAt': null }")
  Page<Users> findAllByDeletedAtIsNull(Pageable pageable);

  @Query(value = "{ 'authorId': ?0, 'deletedAt': null }", count = true)
  long countByAuthorIdAndNotDeleted(String authorId);
}
