package com.clush.planner.application.handler;

import com.clush.planner.application.handler.error.CustomException;
import com.clush.planner.application.handler.error.ErrorCode;
import com.clush.planner.application.handler.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleException(final RuntimeException exception, final HttpServletRequest request) {
    log.error("[Runtime] : ", exception);
    final ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, exception.getMessage());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(final CustomException exception, final HttpServletRequest request) {
    log.error("[" + exception.getErrorCode().getHttpStatus().name() + "] : " + exception.getMessage());
    final ErrorResponse errorResponse = new ErrorResponse(exception.getErrorCode(), exception.getMessage());

    return ResponseEntity.status(exception.getErrorCode().getHttpStatus()).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleException(final MethodArgumentNotValidException exception, final HttpServletRequest request) {
    List<String> errorMessages = exception.getBindingResult()
        .getAllErrors()
        .stream()
        .map(error -> String.format("%s: %s", ((FieldError) error).getField(), error.getDefaultMessage()))
        .toList();

    log.error("[MethodArgumentNotValidException] : " + errorMessages);
    final ErrorResponse errorResponse = new ErrorResponse(ErrorCode.BAD_REQUEST, errorMessages);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }
}
