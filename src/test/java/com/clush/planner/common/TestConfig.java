package com.clush.planner.common;

import com.clush.planner.domain.user.User;
import com.clush.planner.domain.user.UserRepository;
import com.clush.planner.domain.user.dto.JoinRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
public class TestConfig {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  PasswordEncoder passwordEncoder;

  @PostConstruct
  public void setup() {
    JoinRequest joinRequest = new JoinRequest("test name", "test uid1234", "test password");
    User user = User.from(joinRequest, passwordEncoder);
    userRepository.save(user);
  }
}
