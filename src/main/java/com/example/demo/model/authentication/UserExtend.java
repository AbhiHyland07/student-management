package com.example.demo.model.authentication;

import com.example.demo.model.Users;
import org.springframework.security.core.userdetails.User;

public class UserExtend extends User {
  private final String id;
  private final User user;
  private final Users userDocument;

  public UserExtend(User user, String id, Users userDocument) {
    super(user.getUsername(), user.getPassword(), user.getAuthorities());
    this.id = id;
    this.user = user;
    this.userDocument = userDocument;
  }

  public String getId() {
    return id;
  }

  public Users getUserDocument() {
    return userDocument;
  }

  @Override
  public String getUsername() {
    return user.getUsername(); // Return the username from the User object
  }

  @Override
  public String getPassword() {
    return user.getPassword(); // Return the password from the User object
  }
}
