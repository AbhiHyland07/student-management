package com.example.demo.service;

import com.example.demo.configuration.JwtConfig.JwtUtil;
import com.example.demo.dto.CreateUserRequestDto;
import com.example.demo.dto.InviteUserRequestDto;
import com.example.demo.dto.UpdateUserRequestDto;
import com.example.demo.dto.UserListResponseDto;
import com.example.demo.dto.UsersDto;
import com.example.demo.exception.model.AuthenticationFailedException;
import com.example.demo.exception.model.InvalidRequestException;
import com.example.demo.exception.model.ResourceAlreadyPresent;
import com.example.demo.exception.model.ResourceNotFound;
import com.example.demo.exception.model.TokenExpiredException;
import com.example.demo.exception.model.TokenInvalidException;
import com.example.demo.mapper.UsersMapper;
import com.example.demo.model.Authors;
import com.example.demo.model.Users;
import com.example.demo.model.authentication.AuthenticationRequest;
import com.example.demo.model.authentication.AuthenticationResponse;
import com.example.demo.model.authentication.ForgotPasswordRequest;
import com.example.demo.model.authentication.LogoutRequest;
import com.example.demo.model.authentication.RefreshTokenRequest;
import com.example.demo.model.authentication.ResetPasswordRequest;
import com.example.demo.model.authentication.UserExtend;
import com.example.demo.model.enums.Status;
import com.example.demo.repository.AuthorsRepository;
import com.example.demo.repository.UsersRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final JwtUtil jwtTokenUtil;
  private final UserAuthenticationService userAuthentication;
  private final AuthenticationManager authenticationManager;
  private final UsersRepository usersRepository;
  private final AuthorsRepository authorsRepository;
  private final PasswordEncoder passwordEncoder;
  private final MongoTemplate mongoTemplate;

  @Autowired
  public UserService(
      JwtUtil jwtTokenUtil,
      UserAuthenticationService userAuthentication,
      AuthenticationManager authenticationManager,
      UsersRepository usersRepository,
      AuthorsRepository authorsRepository,
      PasswordEncoder passwordEncoder,
      MongoTemplate mongoTemplate) {
    this.jwtTokenUtil = jwtTokenUtil;
    this.userAuthentication = userAuthentication;
    this.authenticationManager = authenticationManager;
    this.usersRepository = usersRepository;
    this.authorsRepository = authorsRepository;
    this.passwordEncoder = passwordEncoder;
    this.mongoTemplate = mongoTemplate;
  }

  public AuthenticationResponse authenticateUser(
      AuthenticationRequest authenticationRequest,
      HttpServletResponse response,
      HttpServletRequest request) {
    try {
      authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
    } catch (Exception _) {
      throw new AuthenticationFailedException("User Credential does not match");
    }
    final UserDetails userDetails =
        userAuthentication.loadUserByUsername(authenticationRequest.getEmail());
    UserExtend userExtend = (UserExtend) userDetails;
    final String accessToken = jwtTokenUtil.generateToken(userExtend);
    final String refreshToken = jwtTokenUtil.generateRefreshToken(userExtend);
    Users user = userAuthentication.loadUserDocumentByEmail(authenticationRequest.getEmail());
    UsersDto userDto = UsersMapper.toDto(user);
    return new AuthenticationResponse(
        accessToken, refreshToken, jwtTokenUtil.extractExpirationAt(accessToken), userDto);
  }

  public AuthenticationResponse refreshAuthentication(RefreshTokenRequest refreshTokenRequest) {
    String refreshToken = refreshTokenRequest.getRefreshToken();
    if (refreshToken == null || refreshToken.isBlank()) {
      throw new InvalidRequestException("Refresh token is required");
    }

    String email = jwtTokenUtil.extractUsername(refreshToken);
    UserExtend userExtend = (UserExtend) userAuthentication.loadUserByUsername(email);
    if (Boolean.FALSE.equals(jwtTokenUtil.validateRefreshToken(refreshToken, userExtend))) {
      throw new TokenInvalidException("Refresh token is invalid or expired");
    }

    jwtTokenUtil.invalidateRefreshToken(refreshToken);
    String accessTokenToInvalidate = refreshTokenRequest.getAccessToken();
    if (accessTokenToInvalidate != null && !accessTokenToInvalidate.isBlank()) {
      jwtTokenUtil.invalidateAccessToken(accessTokenToInvalidate);
    }
    String newAccessToken = jwtTokenUtil.generateToken(userExtend);
    String newRefreshToken = jwtTokenUtil.generateRefreshToken(userExtend);
    Users user = userAuthentication.loadUserDocumentByEmail(email);
    UsersDto userDto = UsersMapper.toDto(user);
    return new AuthenticationResponse(
        newAccessToken, newRefreshToken, jwtTokenUtil.extractExpirationAt(newAccessToken), userDto);
  }

  public void logoutUser(LogoutRequest logoutRequest) {
    String refreshToken = logoutRequest.getRefreshToken();
    if (refreshToken == null || refreshToken.isBlank()) {
      throw new InvalidRequestException("Refresh token is required");
    }
    jwtTokenUtil.invalidateRefreshToken(refreshToken);
    String accessToken = logoutRequest.getAccessToken();
    if (accessToken != null && !accessToken.isBlank()) {
      jwtTokenUtil.invalidateAccessToken(accessToken);
    }
  }

  public UsersDto getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null
        || !(authentication.getPrincipal() instanceof UserExtend userExtend)) {
      throw new ResourceNotFound("No authenticated user found");
    }
    return UsersMapper.toDto(userExtend.getUserDocument());
  }

  public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
    String email = forgotPasswordRequest.getEmail();
    if (email == null || email.isBlank()) {
      throw new InvalidRequestException("Email is required");
    }

    Users user = userAuthentication.loadUserDocumentByEmail(email);
    user.setPasswordResetToken(UUID.randomUUID().toString());
    user.setPasswordResetTokenExpiresAt(Instant.now().plus(15, ChronoUnit.MINUTES));
    user.setUpdatedAt(Instant.now());
    usersRepository.save(user);
  }

  public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
    String token = resetPasswordRequest.getToken();
    String newPassword = resetPasswordRequest.getNewPassword();
    if (token == null || token.isBlank()) {
      throw new InvalidRequestException("Reset token is required");
    }
    if (newPassword == null || newPassword.isBlank()) {
      throw new InvalidRequestException("New password is required");
    }

    Users user =
        usersRepository
            .findByPasswordResetToken(token)
            .orElseThrow(() -> new TokenInvalidException("Reset token is invalid"));
    Instant expiresAt = user.getPasswordResetTokenExpiresAt();
    if (expiresAt == null || expiresAt.isBefore(Instant.now())) {
      throw new TokenExpiredException("Reset token has expired");
    }

    user.setPasswordHash(passwordEncoder.encode(newPassword));
    user.setPasswordResetToken(null);
    user.setPasswordResetTokenExpiresAt(null);
    user.setUpdatedAt(Instant.now());
    usersRepository.save(user);
  }

  public UsersDto createUser(CreateUserRequestDto request) {
    if (usersRepository.findByEmail(request.getEmail()).isPresent()) {
      throw new ResourceAlreadyPresent("User with email " + request.getEmail() + " already exists");
    }
    Users user = new Users();
    user.setFullName(request.getFullName());
    user.setEmail(request.getEmail());
    user.setRole(request.getRole());
    user.setAvatarUrl(request.getAvatarUrl());
    user.setStatus(Status.ACTIVE);
    user.setPreferredLanguage(request.getPreferredLanguage());
    user.setPermissions(request.getPermissions());
    user.setId(UUID.randomUUID().toString());
    user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
    user.setCreatedAt(Instant.now());
    user.setUpdatedAt(Instant.now());
    user.setDeletedAt(null);
    attachAuthorIdIfProvided(user, request.getAuthorId());
    user.setAuthorId(request.getAuthorId());
    Users savedUser = usersRepository.save(user);
    return UsersMapper.toDto(savedUser);
  }

  public UsersDto inviteUser(InviteUserRequestDto request) {
    if (usersRepository.findByEmail(request.getEmail()).isPresent()) {
      throw new ResourceAlreadyPresent("User with email " + request.getEmail() + " already exists");
    }
    Users user = new Users();
    user.setFullName(request.getFullName());
    user.setEmail(request.getEmail());
    user.setRole(request.getRole());
    user.setAvatarUrl(request.getAvatarUrl());
    user.setStatus(Status.INVITED);
    user.setPreferredLanguage(request.getPreferredLanguage());
    user.setPermissions(request.getPermissions());
    user.setCreatedAt(Instant.now());
    user.setUpdatedAt(Instant.now());
    user.setDeletedAt(null);
    attachAuthorIdIfProvided(user, request.getAuthorId());
    return UsersMapper.toDto(usersRepository.save(user));
  }

  public UserListResponseDto getUsers(Integer page, Integer pageSize, String search) {
    int resolvedPage = page == null || page < 1 ? 1 : page;
    int resolvedPageSize = pageSize == null || pageSize < 1 ? 20 : pageSize;

    Query query = new Query();
    query.addCriteria(Criteria.where("deletedAt").is(null));
    if (search != null && !search.isBlank()) {
      String escapedSearch = Pattern.quote(search.trim());
      query.addCriteria(
          new Criteria()
              .orOperator(
                  Criteria.where("fullName").regex(escapedSearch, "i"),
                  Criteria.where("email").regex(escapedSearch, "i")));
    }
    query.with(Sort.by(Sort.Direction.DESC, "createdAt"));
    query.skip((long) (resolvedPage - 1) * resolvedPageSize);
    query.limit(resolvedPageSize);

    UserListResponseDto response = new UserListResponseDto();
    response.setItems(
        mongoTemplate.find(query, Users.class).stream().map(UsersMapper::toDto).toList());
    response.setTotal(response.getItems().size());
    response.setPage(resolvedPage);
    response.setPageSize(resolvedPageSize);
    return response;
  }

  public UsersDto updateUser(String id, UpdateUserRequestDto userUpdates) {
    Users user =
        usersRepository.findById(id).orElseThrow(() -> new ResourceNotFound("User not found"));
    if (user.isDeleted()) {
      throw new ResourceNotFound("User not found");
    }

    if (userUpdates.getFullName() != null && !userUpdates.getFullName().isBlank()) {
      user.setFullName(userUpdates.getFullName());
    }
    if (userUpdates.getAvatarUrl() != null) {
      user.setAvatarUrl(userUpdates.getAvatarUrl());
    }
    if (userUpdates.getRole() != null) {
      user.setRole(userUpdates.getRole());
    }
    if (userUpdates.getStatus() != null) {
      user.setStatus(userUpdates.getStatus());
    }
    if (userUpdates.getPreferredLanguage() != null) {
      user.setPreferredLanguage(userUpdates.getPreferredLanguage());
    }
    if (userUpdates.getPermissions() != null) {
      user.setPermissions(userUpdates.getPermissions());
    }
    user.setUpdatedAt(Instant.now());
    Users updatedUser = usersRepository.save(user);
    return UsersMapper.toDto(updatedUser);
  }

  public void suspendUser(String id) {
    Users user =
        usersRepository.findById(id).orElseThrow(() -> new ResourceNotFound("User not found"));
    if (user.isDeleted()) {
      throw new ResourceNotFound("User not found");
    }
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null
        && authentication.getPrincipal() instanceof UserExtend userExtend
        && userExtend.getUserDocument().getId().equals(id)) {
      throw new InvalidRequestException("You cannot suspend your own account");
    }
    user.setStatus(Status.SUSPENDED);
    user.setUpdatedAt(Instant.now());
    usersRepository.save(user);
  }

  public void reactivateUser(String id) {
    Users user =
        usersRepository.findById(id).orElseThrow(() -> new ResourceNotFound("User not found"));
    if (user.isDeleted()) {
      throw new ResourceNotFound("User not found");
    }
    if (!Status.SUSPENDED.equals(user.getStatus())) {
      throw new InvalidRequestException("User is not suspended and cannot be reactivated");
    }
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null
        && authentication.getPrincipal() instanceof UserExtend userExtend
        && userExtend.getUserDocument().getId().equals(id)) {
      throw new InvalidRequestException("You cannot reactivate your own account");
    }
    user.setStatus(Status.ACTIVE);
    user.setUpdatedAt(Instant.now());
    usersRepository.save(user);
  }

  public void deleteUser(String id) {
    Users user =
        usersRepository
            .findByIdAndNotDeleted(id)
            .orElseThrow(() -> new ResourceNotFound("User not found"));
    user.setDeletedAt(Instant.now());
    usersRepository.save(user);
  }

  private void attachAuthorIdIfProvided(Users user, String authorId) {
    if (StringUtils.isBlank(authorId)) {
      throw new InvalidRequestException("Author ID is required");
    }
    Authors author =
        authorsRepository
            .findByIdAndNotDeleted(authorId)
            .orElseThrow(() -> new ResourceNotFound("Author not found or is deleted"));
    user.setAuthorId(author.getId());
  }

  private void authenticate(String email, String password) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    } catch (DisabledException _) {
      throw new AuthenticationFailedException("User Disabled");
    } catch (BadCredentialsException _) {
      throw new AuthenticationFailedException("Bad Credentials");
    }
  }
}
