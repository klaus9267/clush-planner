package com.clush.planner.domain.user;

import com.clush.planner.application.auth.SecurityUtil;
import com.clush.planner.application.handler.error.CustomException;
import com.clush.planner.application.handler.error.ErrorCode;
import com.clush.planner.domain.user.dto.JoinRequest;
import com.clush.planner.domain.user.dto.LoginRequest;
import com.clush.planner.domain.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final SecurityUtil securityUtil;

  @Transactional
  public void join(final JoinRequest joinRequest) {
    userRepository.findByUid(joinRequest.uid()).ifPresent(user -> {
      throw new CustomException(ErrorCode.CONFLICT_UID);
    });

    final User user = User.from(joinRequest, passwordEncoder);
    userRepository.save(user);
  }

  public UserResponse readCurrentUserInfo() {
    final Long currentUserId = securityUtil.getCurrentUserId();
    final User user = userRepository.findById(currentUserId).orElseThrow(ErrorCode.NOT_FOUND_USER);

    return UserResponse.from(user);
  }

  public List<UserResponse> readUsersInfo(final List<Long> userIds) {
    final List<User> users = userRepository.findAllById(userIds);
    return UserResponse.from(users);
  }

  public User readUser(final long id) {
    return userRepository.findById(id).orElseThrow(ErrorCode.NOT_FOUND_USER);
  }

  public List<User> readUsers(final List<Long> userIds) {
    return userRepository.findAllById(userIds);
  }

  public User login(final LoginRequest loginRequest) {
    return userRepository.findByUid(loginRequest.uid())
        .filter(user -> passwordEncoder.matches(loginRequest.password(), user.getPassword()))
        .orElseThrow(ErrorCode.NOT_FOUND_USER);
  }

  @Transactional
  public void updateUser(final String name) {
    final Long currentUserId = securityUtil.getCurrentUserId();
    final User user = userRepository.findById(currentUserId).orElseThrow(ErrorCode.NOT_FOUND_USER);
    user.updateName(name);
  }

  @Transactional
  public void deleteUser() {
    final Long currentUserId = securityUtil.getCurrentUserId();
    final User user = userRepository.findById(currentUserId).orElseThrow(ErrorCode.NOT_FOUND_USER);
    userRepository.delete(user);
  }
}
