package com.example.demo.repository;

import com.example.demo.model.Users;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepository extends MongoRepository<Users, String> {
  Optional<Users> findByEmail(String email);

  Optional<Users> findByPasswordResetToken(String passwordResetToken);
}
