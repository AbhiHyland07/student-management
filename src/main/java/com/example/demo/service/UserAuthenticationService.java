package com.example.demo.service;

import com.example.demo.model.Users;
import com.example.demo.model.authentication.UserExtend;
import com.example.demo.repository.UsersRepository;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationService implements UserDetailsService {
  private final UsersRepository usersRepository;

  @Autowired
  public UserAuthenticationService(UsersRepository usersRepository) {
    this.usersRepository = usersRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Users user =
        usersRepository
            .findByEmail(email)
            .orElseThrow(
                () -> new UsernameNotFoundException("User not found with email: " + email));
    User securityUser =
        new User(
            user.getEmail(),
            user.getPasswordHash(),
            Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));
    return new UserExtend(securityUser, user.getId(), user);
  }

  public Users loadUserDocumentByEmail(String email) {
    return usersRepository
        .findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
  }
}
