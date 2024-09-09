package com.clush.planner.IntegrationTest;

import com.clush.planner.common.TestConfig;
import com.clush.planner.common.WithCustomMockUser;
import com.clush.planner.domain.team.Team;
import com.clush.planner.domain.team.TeamRepository;
import com.clush.planner.domain.user.User;
import com.clush.planner.domain.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@AutoConfigureMockMvc
@Import(TestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TeamIntegrationTest {
  private static final String END_POINT = "/api/teams";
  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  protected ObjectMapper objectMapper;
  @Autowired
  UserRepository userRepository;
  @Autowired
  TeamRepository teamRepository;
  @Autowired
  EntityManager entityManager;

  @Test
  @Order(1)
  @DisplayName("팀_생성")
  @WithCustomMockUser
  void createTeam() throws Exception {
    String teamName = "newTeam";

    mockMvc.perform(post(END_POINT + "/" + teamName))
        .andDo(print())
        .andExpect(status().isCreated());

    Team team = teamRepository.findById(1L).orElseThrow(NoSuchElementException::new);

    assertThat(team.getName()).isEqualTo(teamName);
  }

  @Test
  @Order(2)
  @DisplayName("팀_참가")
  @WithCustomMockUser
  void joinTeam() throws Exception {
    Team team = teamRepository.save(Team.from("new team"));

    mockMvc.perform(post(END_POINT + "/join/" + team.getId()))
        .andDo(print())
        .andExpect(status().isNoContent());

    Team joinedTeam = teamRepository.findById(team.getId()).orElseThrow(NoSuchElementException::new);

    assertThat(joinedTeam.getUsers()).hasSize(1);
  }

  @Test
  @Order(3)
  @DisplayName("팀_정보_조회")
  @WithCustomMockUser
  void readTeamInfo() throws Exception {
    Team team = teamRepository.save(Team.from("new team"));

    mockMvc.perform(get(END_POINT + "/" + team.getId()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(team.getId()))
        .andExpect(jsonPath("$.name").value(team.getName()));
  }

  @Test
  @Order(4)
  @DisplayName("팀_정보_수정")
  @WithCustomMockUser
  void updateTeam() throws Exception {
    Team team = teamRepository.save(Team.from("new team"));
    User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);
    team.adduser(user);
    user.joinTeam(team);

    String newName = "new-team2";

    mockMvc.perform(patch(END_POINT)
            .param("id", String.valueOf(team.getId()))
            .param("name", newName)
        )
        .andDo(print())
        .andExpect(status().isNoContent());

    Team updatedTeam = teamRepository.findById(team.getId()).orElseThrow(NoSuchElementException::new);

    assertThat(updatedTeam.getName()).isEqualTo(newName);
  }

  @Test
  @Order(5)
  @DisplayName("팀_삭제")
  @WithCustomMockUser
  void deleteTeam() throws Exception {
    Team team = teamRepository.save(Team.from("new team"));
    User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);
    team.adduser(user);
    user.joinTeam(team);

    mockMvc.perform(delete(END_POINT + "/" + team.getId()))
        .andDo(print())
        .andExpect(status().isNoContent());

    Optional<Team> optionalTeam = teamRepository.findById(team.getId());
    assertThatThrownBy(optionalTeam::get)
        .isInstanceOf(Exception.class);
  }

  @Test
  @Order(6)
  @DisplayName("팀_탈퇴")
  @WithCustomMockUser
  void leaveTeam() throws Exception {
    Team team = teamRepository.save(Team.from("new team"));
    User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);
    team.adduser(user);
    user.joinTeam(team);

    mockMvc.perform(delete(END_POINT + "/leave"))
        .andDo(print())
        .andExpect(status().isNoContent());

    Team foundTeam = teamRepository.findById(team.getId()).orElseThrow(NoSuchElementException::new);
    assertThat(foundTeam.getUsers()).isEmpty();
  }
}
