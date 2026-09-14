package com.example.demo.controller;

import com.example.demo.dto.UsersDto;
import com.example.demo.model.authentication.AuthenticationRequest;
import com.example.demo.model.authentication.AuthenticationResponse;
import com.example.demo.model.authentication.ForgotPasswordRequest;
import com.example.demo.model.authentication.LogoutRequest;
import com.example.demo.model.authentication.RefreshTokenRequest;
import com.example.demo.model.authentication.ResetPasswordRequest;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
  private final UserService userService;

  public AuthenticationController(UserService userService) {
    this.userService = userService;
  }

  @Operation(
      summary = "Fetching a JWT Token",
      description =
          "This accept authentication request body and return a jwt token for correct credentials",
      parameters = {
        @Parameter(
            name = "Authentication Request",
            description = "Accept a Authentication request object",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AuthenticationRequest.class)))
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved the JWT Token",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AuthenticationResponse.class))),
      })
  @PostMapping("/login")
  public ResponseEntity<?> createAuthenticationToken(
      @RequestBody AuthenticationRequest authenticationRequest,
      HttpServletResponse response,
      HttpServletRequest request) {
    return ResponseEntity.ok(
        userService.authenticateUser(authenticationRequest, response, request));
  }

  @PostMapping("/refresh")
  public ResponseEntity<AuthenticationResponse> refreshAuthentication(
      @RequestBody RefreshTokenRequest refreshTokenRequest) {
    return ResponseEntity.ok(userService.refreshAuthentication(refreshTokenRequest));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@RequestBody LogoutRequest logoutRequest) {
    userService.logoutUser(logoutRequest);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/me")
  public ResponseEntity<UsersDto> getCurrentUser() {
    return ResponseEntity.ok(userService.getCurrentUser());
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<Void> forgotPassword(
      @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
    userService.forgotPassword(forgotPasswordRequest);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/reset-password")
  public ResponseEntity<Void> resetPassword(
      @RequestBody ResetPasswordRequest resetPasswordRequest) {
    userService.resetPassword(resetPasswordRequest);
    return ResponseEntity.noContent().build();
  }
}
