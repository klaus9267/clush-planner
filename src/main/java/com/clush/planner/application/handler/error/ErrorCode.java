package com.clush.planner.application.handler.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public enum ErrorCode implements Supplier<CustomException> {
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다"),

  INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다"),
  EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다"),
  UNSUPPORTED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다"),
  LOGIN_REQUIRED(HttpStatus.FORBIDDEN, "로그인이 필요한 API 입니다."),

  EMPTY_JWT_CLAIMS(HttpStatus.BAD_REQUEST, "JWT 클레임이 비어있습니다"),
  NOT_FOUND(HttpStatus.NOT_FOUND, "해당 데이터를 찾을 수 없습니다"),
  NOT_FOUND_USER(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다"),

  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 서버 에러입니다"),
  ;

  private final HttpStatus httpStatus;
  private final String message;

  @Override
  public CustomException get() {
    return new CustomException(this);
  }
}
