package com.clush.planner.application.handler.error;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResponse {
  private final int statusCode;
  private final String error;
  private final List<String> message = new ArrayList<>();

  public ErrorResponse(final ErrorCode errorCode) {
    this.statusCode = errorCode.getHttpStatus().value();
    this.error = errorCode.getHttpStatus().name();
    this.message.add(errorCode.getMessage());
  }

  public ErrorResponse(final ErrorCode errorCode, final String message) {
    this.statusCode = errorCode.getHttpStatus().value();
    this.error = errorCode.getHttpStatus().name();
    this.message.add(message);
  }

  public ErrorResponse(final ErrorCode errorCode, final List<String> message) {
    this.statusCode = errorCode.getHttpStatus().value();
    this.error = errorCode.getHttpStatus().name();
    this.message.addAll(message);
  }
}
