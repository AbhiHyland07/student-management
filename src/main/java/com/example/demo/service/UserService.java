package com.example.demo.service;

import com.example.demo.configuration.JwtConfig.JwtUtil;
import com.example.demo.dto.UsersDto;
import com.example.demo.mapper.UsersMapper;
import com.example.demo.model.Users;
import com.example.demo.model.authentication.AuthenticationRequest;
import com.example.demo.model.authentication.AuthenticationResponse;
import com.example.demo.model.authentication.ForgotPasswordRequest;
import com.example.demo.model.authentication.LogoutRequest;
import com.example.demo.model.authentication.RefreshTokenRequest;
import com.example.demo.model.authentication.ResetPasswordRequest;
import com.example.demo.model.authentication.UserExtend;
import com.example.demo.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
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
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserService(
      JwtUtil jwtTokenUtil,
      UserAuthenticationService userAuthentication,
      AuthenticationManager authenticationManager,
      UsersRepository usersRepository,
      PasswordEncoder passwordEncoder) {
    this.jwtTokenUtil = jwtTokenUtil;
    this.userAuthentication = userAuthentication;
    this.authenticationManager = authenticationManager;
    this.usersRepository = usersRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public AuthenticationResponse authenticateUser(
      AuthenticationRequest authenticationRequest,
      HttpServletResponse response,
      HttpServletRequest request) {
    try {
      authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
    } catch (Exception e) {
      throw new RuntimeException("User Credential does not match");
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
      throw new RuntimeException("Refresh token is required");
    }

    String email = jwtTokenUtil.extractUsername(refreshToken);
    UserExtend userExtend = (UserExtend) userAuthentication.loadUserByUsername(email);
    if (!jwtTokenUtil.validateRefreshToken(refreshToken, userExtend)) {
      throw new RuntimeException("Refresh token is invalid or expired");
    }

    String newAccessToken = jwtTokenUtil.generateToken(userExtend);
    String newRefreshToken = jwtTokenUtil.generateRefreshToken(userExtend);
    jwtTokenUtil.invalidateRefreshToken(refreshToken);
    Users user = userAuthentication.loadUserDocumentByEmail(email);
    UsersDto userDto = UsersMapper.toDto(user);
    return new AuthenticationResponse(
        newAccessToken, newRefreshToken, jwtTokenUtil.extractExpirationAt(newAccessToken), userDto);
  }

  public void logoutUser(LogoutRequest logoutRequest) {
    String refreshToken = logoutRequest.getRefreshToken();
    if (refreshToken == null || refreshToken.isBlank()) {
      throw new RuntimeException("Refresh token is required");
    }
    jwtTokenUtil.invalidateRefreshToken(refreshToken);
  }

  public UsersDto getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null
        || !(authentication.getPrincipal() instanceof UserExtend userExtend)) {
      throw new RuntimeException("No authenticated user found");
    }
    return UsersMapper.toDto(userExtend.getUserDocument());
  }

  public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
    String email = forgotPasswordRequest.getEmail();
    if (email == null || email.isBlank()) {
      throw new RuntimeException("Email is required");
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
      throw new RuntimeException("Reset token is required");
    }
    if (newPassword == null || newPassword.isBlank()) {
      throw new RuntimeException("New password is required");
    }

    Users user =
        usersRepository
            .findByPasswordResetToken(token)
            .orElseThrow(() -> new RuntimeException("Reset token is invalid"));
    Instant expiresAt = user.getPasswordResetTokenExpiresAt();
    if (expiresAt == null || expiresAt.isBefore(Instant.now())) {
      throw new RuntimeException("Reset token has expired");
    }

    user.setPasswordHash(passwordEncoder.encode(newPassword));
    user.setPasswordResetToken(null);
    user.setPasswordResetTokenExpiresAt(null);
    user.setUpdatedAt(Instant.now());
    usersRepository.save(user);
  }

  private void authenticate(String email, String password) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    } catch (DisabledException e) {
      throw new RuntimeException("User Disabled");
    } catch (BadCredentialsException e) {
      throw new RuntimeException("Bad Credentials");
    }
  }
}
