package com.example.demo.configuration.JwtConfig;

import com.example.demo.model.authentication.UserExtend;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
  private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512); // Use a stronger
  // secret key in
  // production
  private static final long ACCESS_TOKEN_EXPIRATION_TIME = 86400000; // 1 day in milliseconds
  private static final long REFRESH_TOKEN_EXPIRATION_TIME = 604800000; // 7 days in milliseconds
  private final Set<String> invalidatedRefreshTokens =
      Collections.newSetFromMap(new ConcurrentHashMap<>());
  private final Set<String> invalidatedAccessTokens =
      Collections.newSetFromMap(new ConcurrentHashMap<>());

  // Generate JWT token for a user
  public String generateToken(UserExtend userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", userDetails.getAuthorities());
    claims.put("id", userDetails.getId());
    claims.put("user", userDetails.getUsername());
    claims.put("type", "access");
    return createToken(claims, userDetails.getUsername(), ACCESS_TOKEN_EXPIRATION_TIME);
  }

  public String generateRefreshToken(UserExtend userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("id", userDetails.getId());
    claims.put("user", userDetails.getUsername());
    claims.put("type", "refresh");
    return createToken(claims, userDetails.getUsername(), REFRESH_TOKEN_EXPIRATION_TIME);
  }

  // Create the JWT token
  private String createToken(Map<String, Object> claims, String subject, long expirationTime) {
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();
  }

  // Extract username from JWT token
  public String extractUsername(String token) {
    return extractClaims(token).getSubject();
  }

  public Instant extractExpirationAt(String token) {
    return extractExpiration(token).toInstant();
  }

  public String extractTokenType(String token) {
    return extractClaims(token).get("type", String.class);
  }

  // Extract all claims from JWT token
  private Claims extractClaims(String token) {
    return Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
  }

  // Validate JWT token
  public boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  // Extract expiration date from JWT token
  private Date extractExpiration(String token) {
    return extractClaims(token).getExpiration();
  }

  public Boolean validateToken(String token, UserExtend userDetails) {
    final String email = extractUsername(token);
    return (email.equals(userDetails.getUsername())
        && !isTokenExpired(token)
        && !isAccessTokenInvalidated(token));
  }

  public Boolean validateRefreshToken(String token, UserExtend userDetails) {
    return validateToken(token, userDetails)
        && "refresh".equals(extractTokenType(token))
        && !isRefreshTokenInvalidated(token);
  }

  public void invalidateRefreshToken(String token) {
    invalidatedRefreshTokens.add(token);
  }

  public void invalidateAccessToken(String token) {
    invalidatedAccessTokens.add(token);
  }

  public boolean isRefreshTokenInvalidated(String token) {
    return invalidatedRefreshTokens.contains(token);
  }

  public boolean isAccessTokenInvalidated(String token) {
    return invalidatedAccessTokens.contains(token);
  }
}
