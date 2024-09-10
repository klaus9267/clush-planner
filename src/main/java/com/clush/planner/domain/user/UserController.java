package com.clush.planner.domain.user;

import com.clush.planner.application.auth.JwtUtil;
import com.clush.planner.domain.common.swagger.SwaggerCreated;
import com.clush.planner.domain.common.swagger.SwaggerNoContent;
import com.clush.planner.domain.common.swagger.SwaggerOK;
import com.clush.planner.domain.user.dto.JoinRequest;
import com.clush.planner.domain.user.dto.LoginRequest;
import com.clush.planner.domain.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "USER API")
public class UserController {
  private final UserService userService;
  private final JwtUtil jwtUtil;

  @PostMapping
  @SwaggerCreated(summary = "회원가입 API")
  public void join(@Valid @RequestBody final JoinRequest joinRequest) {
    userService.join(joinRequest);
  }

  @PostMapping("login")
  @SwaggerOK(summary = "로그인 API")
  public UserResponse login(@Valid @RequestBody final LoginRequest loginRequest) {
    final User user = userService.login(loginRequest);
    final String token = jwtUtil.createAccessToken(user);

    return UserResponse.from(user, token);
  }

  @GetMapping
  @SwaggerOK(summary = " 내 정보 조회 API")
  public UserResponse readCurrentUserInfo() {
    return userService.readCurrentUserInfo();
  }

  @GetMapping("search")
  @SwaggerOK(summary = "사용자 검색 API")
  public List<UserResponse> searchUsersInfo(@RequestParam("userIds") final List<Long> userIds) {
    return userService.readUsersInfo(userIds);
  }

  @PatchMapping("{name}")
  @SwaggerNoContent(summary = "내 정보 수정 API")
  public void updateMyInfo(@PathVariable("name") final String name) {
    userService.updateUser(name);
  }

  @DeleteMapping
  @SwaggerNoContent(summary = "회원탈퇴 API")
  public void leavePlanner() {
    userService.deleteUser();
  }
}
