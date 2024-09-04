package com.clush.planner.application.handler.error;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
  private final ErrorCode errorCode;

  public CustomException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public CustomException(ErrorCode errorCode, Exception e) {
    super(e.getMessage());
    this.errorCode = errorCode;
  }
}
