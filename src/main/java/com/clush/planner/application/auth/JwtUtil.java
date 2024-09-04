package com.clush.planner.application.auth;

import com.clush.planner.application.handler.error.CustomException;
import com.clush.planner.application.handler.error.ErrorCode;
import com.clush.planner.domain.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {
  private final Key key;
  private final long accessTokenExpTime;

  public JwtUtil(
      @Value("${jwt.secret}") String secretKey,
      @Value("${jwt.expiration_time}") long accessTokenExpTime
  ) {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
    this.accessTokenExpTime = accessTokenExpTime;
  }

  public String createAccessToken(User user) {
    return createToken(user, accessTokenExpTime);
  }

  private String createToken(User user, long expireTime) {
    Claims claims = Jwts.claims();
    claims.put("userId", user.getId());
    claims.put("role", "USER");

    ZonedDateTime now = ZonedDateTime.now();
    ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(Date.from(now.toInstant()))
        .setExpiration(Date.from(tokenValidity.toInstant()))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public Long getUserId(String token) {
    return this.parseClaims(token).get("userId", Long.class);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      throw new CustomException(ErrorCode.INVALID_JWT_TOKEN);
    } catch (ExpiredJwtException e) {
      throw new CustomException(ErrorCode.EXPIRED_JWT_TOKEN);
    } catch (UnsupportedJwtException e) {
      throw new CustomException(ErrorCode.UNSUPPORTED_JWT_TOKEN);
    } catch (IllegalArgumentException e) {
      throw new CustomException(ErrorCode.EMPTY_JWT_CLAIMS);
    }
  }

  public Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}
