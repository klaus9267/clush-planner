package com.clush.planner.application.auth;

import com.clush.planner.application.handler.error.CustomException;
import com.clush.planner.application.handler.error.ErrorCode;
import com.clush.planner.domain.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {
  public Long getCurrentUserId() {
    return this.getCurrentUser().getId();
  }

  public User getCurrentUser() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication.getName() == null || authentication.getPrincipal().equals("anonymousUser")) {
      final ErrorCode errorCode = authentication == null || authentication.getName() == null ? ErrorCode.NOT_FOUND_USER : ErrorCode.LOGIN_REQUIRED;
      throw new CustomException(errorCode);
    }

    return (User) authentication.getPrincipal();
  }
}
