package com.clush.planner.IntegrationTest;

import com.clush.planner.common.TestConfig;
import com.clush.planner.common.WithCustomMockUser;
import com.clush.planner.domain.user.User;
import com.clush.planner.domain.user.UserRepository;
import com.clush.planner.domain.user.dto.JoinRequest;
import com.clush.planner.domain.user.dto.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@Import(TestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserIntegrationTest {
  private static final String END_POINT = "/api/users";
  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  protected ObjectMapper objectMapper;
  @Autowired
  UserRepository userRepository;

  @Test
  @DisplayName("회원가입")
  void join() throws Exception {
    JoinRequest joinRequest = new JoinRequest("test name", "test uid123", "test password");
    String joinBody = objectMapper.writeValueAsString(joinRequest);

    mockMvc.perform(post(END_POINT)
            .contentType(APPLICATION_JSON)
            .content(joinBody))
        .andDo(print())
        .andExpect(status().isCreated());

    User user = userRepository.findById(2L).orElseThrow(NoSuchFieldException::new);

    assertThat(user.getUid()).isEqualTo(joinRequest.uid());
    assertThat(user.getName()).isEqualTo(joinRequest.name());
  }

  @Test
  @DisplayName("로그인")
  void login() throws Exception {
    User user = userRepository.findById(1L).orElseThrow(NoSuchFieldException::new);

    LoginRequest loginRequest = new LoginRequest(user.getUid(), "test password");
    String loginBody = objectMapper.writeValueAsString(loginRequest);

    mockMvc.perform(post(END_POINT + "/login")
            .contentType(APPLICATION_JSON)
            .content(loginBody))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.id").value(user.getId()))
        .andExpect(jsonPath("$.uid").value(user.getUid()))
        .andExpect(jsonPath("$.name").value(user.getName()));
  }

  @Test
  @DisplayName("내_정보_조회")
  @WithCustomMockUser
  void readCurrentUserInfo() throws Exception {
    final User user = userRepository.findById(1L).orElseThrow(NoSuchFieldException::new);

    mockMvc.perform(get(END_POINT))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.uid").value(user.getUid()))
        .andExpect(jsonPath("$.name").value(user.getName()));
  }

  @Test
  @DisplayName("내_정보_수정")
  @WithCustomMockUser
  void updateMyInfo() throws Exception {
    String newName = "new name";

    mockMvc.perform(patch(END_POINT + "/" + newName))
        .andExpect(status().isNoContent())
        .andDo(print());

    final User user = userRepository.findById(1L).orElseThrow(NoSuchFieldException::new);

    assertThat(user.getName()).isEqualTo(newName);
  }

  @Test
  @DisplayName("회원탈퇴")
  @WithCustomMockUser
  void leavePlanner() throws Exception {
    mockMvc.perform(delete(END_POINT))
        .andExpect(status().isNoContent())
        .andDo(print());

    Optional<User> optionalUser = userRepository.findById(1L);
    assertThatThrownBy(optionalUser::get).isInstanceOf(Exception.class);
  }
}
