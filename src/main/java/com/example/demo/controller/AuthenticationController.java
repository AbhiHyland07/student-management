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
import jakarta.validation.Valid;
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
      @Valid @RequestBody AuthenticationRequest authenticationRequest,
      HttpServletResponse response,
      HttpServletRequest request) {
    return ResponseEntity.ok(
        userService.authenticateUser(authenticationRequest, response, request));
  }

  @Operation(
      summary = "Refresh Authentication Token",
      description = "Generates a new access token and refresh token using a valid refresh token")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully refreshed authentication tokens",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AuthenticationResponse.class))),
        @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
      })
  @PostMapping("/refresh")
  public ResponseEntity<AuthenticationResponse> refreshAuthentication(
      @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
    return ResponseEntity.ok(userService.refreshAuthentication(refreshTokenRequest));
  }

  @Operation(
      summary = "Logout User",
      description =
          "Invalidates the current refresh token, requiring the user to login again for new tokens")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Successfully logged out"),
        @ApiResponse(responseCode = "400", description = "Invalid logout request")
      })
  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest logoutRequest) {
    userService.logoutUser(logoutRequest);
    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "Get Current User",
      description = "Retrieves the profile information of the currently authenticated user")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved current user information",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UsersDto.class))),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
      })
  @GetMapping("/me")
  public ResponseEntity<UsersDto> getCurrentUser() {
    return ResponseEntity.ok(userService.getCurrentUser());
  }

  @Operation(
      summary = "Request Password Reset",
      description =
          "Initiates a password reset by sending a reset token to the registered email address")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Reset email sent successfully"),
        @ApiResponse(responseCode = "400", description = "Email not found in the system")
      })
  @PostMapping("/forgot-password")
  public ResponseEntity<Void> forgotPassword(
      @Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
    userService.forgotPassword(forgotPasswordRequest);
    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "Reset Password",
      description =
          "Resets the user password using a valid reset token received from forgot-password"
              + " endpoint")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Password reset successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid or expired reset token")
      })
  @PostMapping("/reset-password")
  public ResponseEntity<Void> resetPassword(
      @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
    userService.resetPassword(resetPasswordRequest);
    return ResponseEntity.noContent().build();
  }
}
