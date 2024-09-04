package com.clush.planner.application.auth;

import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.password.PasswordEncoder;

@UtilityClass
public class PasswordEncoderFactory {
  private static PasswordEncoder passwordEncoder;

  public static PasswordEncoder getPasswordEncoder() {
    if (passwordEncoder == null) {
      throw new IllegalStateException("PasswordEncoder not initialized");
    }
    return passwordEncoder;
  }
}
