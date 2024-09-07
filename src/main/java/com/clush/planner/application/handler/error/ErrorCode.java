package com.clush.planner.application.handler.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public enum ErrorCode implements Supplier<CustomException> {
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다"),
  INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호를 확인해주세요"),

  INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다"),
  EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다"),
  UNSUPPORTED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다"),
  EMPTY_JWT_CLAIMS(HttpStatus.UNAUTHORIZED, "JWT 클레임이 비어있습니다"),
  LOGIN_REQUIRED(HttpStatus.FORBIDDEN, "로그인이 필요한 API 입니다."),
  INVALID_TEAM_USER(HttpStatus.FORBIDDEN, "팀 멤버가 아닙니다"),
  INVALID_USER(HttpStatus.FORBIDDEN, "잘못된 사용자의 접근입니다"),
  EMPTY_TEAM(HttpStatus.FORBIDDEN, "소속된 팀이 없습니다"),

  NOT_FOUND(HttpStatus.NOT_FOUND, "해당 데이터를 찾을 수 없습니다"),
  NOT_FOUND_USER(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다"),
  NOT_FOUND_TEAM(HttpStatus.NOT_FOUND, "해당 팀을 찾을 수 없습니다"),
  NOT_FOUND_TODO(HttpStatus.NOT_FOUND, "해당 Todo를 찾을 수 없습니다"),

  CONFLICT_UID(HttpStatus.CONFLICT, "사용중인 id 입니다"),
  CONFLICT_TEAM_NAME(HttpStatus.CONFLICT, "사용중인 팀 이름입니다"),
  ALREADY_JOIN(HttpStatus.CONFLICT, "소속된 팀이 있습니다"),

  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 서버 에러입니다"),
  ;

  private final HttpStatus httpStatus;
  private final String message;

  @Override
  public CustomException get() {
    return new CustomException(this);
  }
}
