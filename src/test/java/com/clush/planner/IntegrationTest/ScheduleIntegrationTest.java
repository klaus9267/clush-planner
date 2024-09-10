package com.clush.planner.IntegrationTest;

import com.clush.planner.common.TestConfig;
import com.clush.planner.common.WithCustomMockUser;
import com.clush.planner.domain.common.Importance;
import com.clush.planner.domain.schedule.Schedule;
import com.clush.planner.domain.schedule.dto.ScheduleRequest;
import com.clush.planner.domain.schedule.repository.ScheduleCustomRepository;
import com.clush.planner.domain.schedule.repository.ScheduleRepository;
import com.clush.planner.domain.team.Team;
import com.clush.planner.domain.team.TeamRepository;
import com.clush.planner.domain.user.User;
import com.clush.planner.domain.user.UserRepository;
import com.clush.planner.domain.user.dto.JoinRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@AutoConfigureMockMvc
@Import(TestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ScheduleIntegrationTest {
  private static final String END_POINT = "/api/schedules";
  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  protected ObjectMapper objectMapper;
  @Autowired
  UserRepository userRepository;
  @Autowired
  TeamRepository teamRepository;
  @Autowired
  ScheduleRepository scheduleRepository;
  @Autowired
  ScheduleCustomRepository scheduleCustomRepository;
  @Autowired
  PasswordEncoder passwordEncoder;

  @Test
  @Order(1)
  @DisplayName("일정_생성")
  @WithCustomMockUser
  void createSchedule() throws Exception {
    ScheduleRequest scheduleRequest = new ScheduleRequest("test name", LocalDate.now().plusDays(2), null, null, Importance.HIGH);
    String body = objectMapper.writeValueAsString(scheduleRequest);

    mockMvc.perform(post(END_POINT)
            .contentType(APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isCreated());

    Schedule schedule = scheduleRepository.findById(1L).orElseThrow(NoSuchElementException::new);

    assertThat(schedule.getName()).isEqualTo(scheduleRequest.name());
    assertThat(schedule.getDate()).isEqualTo(scheduleRequest.date());
    assertThat(schedule.getImportance()).isEqualTo(scheduleRequest.importance());
  }

  @Test
  @Order(2)
  @DisplayName("팀_일정_생성")
  @WithCustomMockUser
  void createTeamSchedule() throws Exception {
    Team team = Team.from("new team");
    User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);
    team.adduser(user);
    user.joinTeam(team);
    Team savedTeam = teamRepository.save(team);

    ScheduleRequest scheduleRequest = new ScheduleRequest("test name", LocalDate.now().plusDays(2), null, null, Importance.HIGH);
    String body = objectMapper.writeValueAsString(scheduleRequest);

    mockMvc.perform(post(END_POINT + "/" + team.getId())
            .contentType(APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isCreated());

    List<Schedule> schedules = scheduleRepository.findAll();

    assertThat(schedules.get(0).getName()).isEqualTo(scheduleRequest.name());
    assertThat(schedules.get(0).getDate()).isEqualTo(scheduleRequest.date());
    assertThat(schedules.get(0).getImportance()).isEqualTo(scheduleRequest.importance());
    assertThat(schedules.get(0).getTeam().getId()).isEqualTo(savedTeam.getId());
  }

  @Test
  @Order(3)
  @DisplayName("일정_공유(사용자)")
  @WithCustomMockUser
  void shareScheduleToUsers() throws Exception {
    List<User> users = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      JoinRequest joinRequest = new JoinRequest("test name" + i, "test uid1234" + i, "test password");
      User user = User.from(joinRequest, passwordEncoder);
      users.add(user);
    }
    List<User> savedUsers = userRepository.saveAll(users);
    List<Long> userIds = savedUsers.stream().map(User::getId).toList();

    ScheduleRequest scheduleRequest = new ScheduleRequest("test name", LocalDate.now().plusDays(2), null, null, Importance.HIGH);
    User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);
    Schedule savedSchedule = scheduleRepository.save(Schedule.from(scheduleRequest, user));

    mockMvc.perform(post(END_POINT + "/share")
            .param("userIds", userIds.stream().map(String::valueOf).toArray(String[]::new))
            .param("scheduleId", String.valueOf(savedSchedule.getId())))
        .andDo(print())
        .andExpect(status().isCreated());

    List<Schedule> schedules = scheduleRepository.findAll();
    assertThat(schedules).hasSize(users.size() + 1);
  }

  @Test
  @Order(4)
  @DisplayName("일정_공유(팀)")
  @WithCustomMockUser
  void shareScheduleToTeam() throws Exception {
    Team team = Team.from("new team");
    Team savedTeam = teamRepository.save(team);

    List<User> users = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      JoinRequest joinRequest = new JoinRequest("test name" + i, "test uid1234" + i, "test password");
      User user = User.from(joinRequest, passwordEncoder);
      savedTeam.adduser(user);
      user.joinTeam(team);

      users.add(user);
    }
    userRepository.saveAll(users);

    ScheduleRequest scheduleRequest = new ScheduleRequest("test name", LocalDate.now().plusDays(2), null, null, Importance.HIGH);
    User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);
    Schedule savedSchedule = scheduleRepository.save(Schedule.from(scheduleRequest, user));

    mockMvc.perform(post(END_POINT + "/share/team")
            .param("teamId", String.valueOf(savedTeam.getId()))
            .param("scheduleId", String.valueOf(savedSchedule.getId())))
        .andDo(print())
        .andExpect(status().isCreated());

    List<Schedule> schedules = scheduleRepository.findAll();
    assertThat(schedules).hasSize(users.size() + 1);
    for (int i = 1; i < schedules.size(); i++) {
      assertThat(schedules.get(i).getTeam().getId()).isEqualTo(savedTeam.getId());
    }
  }
}
