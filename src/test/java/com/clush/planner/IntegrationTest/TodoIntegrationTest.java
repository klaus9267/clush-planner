package com.clush.planner.IntegrationTest;

import com.clush.planner.common.TestConfig;
import com.clush.planner.common.WithCustomMockUser;
import com.clush.planner.domain.common.Importance;
import com.clush.planner.domain.team.Team;
import com.clush.planner.domain.team.TeamRepository;
import com.clush.planner.domain.todo.Todo;
import com.clush.planner.domain.todo.dto.TodoRequest;
import com.clush.planner.domain.todo.repository.TodoCustomRepository;
import com.clush.planner.domain.todo.repository.TodoRepository;
import com.clush.planner.domain.user.User;
import com.clush.planner.domain.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@AutoConfigureMockMvc
@Import(TestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoIntegrationTest {
  private static final String END_POINT = "/api/todos";
  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  protected ObjectMapper objectMapper;
  @Autowired
  UserRepository userRepository;
  @Autowired
  TeamRepository teamRepository;
  @Autowired
  TodoRepository todoRepository;
  @Autowired
  TodoCustomRepository todoCustomRepository;

  @Test
  @Order(1)
  @DisplayName("Todo_생성")
  @WithCustomMockUser
  void createTodo() throws Exception {
    TodoRequest todoRequest = new TodoRequest("todo name", LocalDateTime.now().minusDays(1), Importance.MIDDLE);
    String body = objectMapper.writeValueAsString(todoRequest);

    mockMvc.perform(post(END_POINT)
            .contentType(APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isCreated());

    Todo todo = todoRepository.findById(1L).orElseThrow(NoSuchElementException::new);
    assertThat(todo.getName()).isEqualTo(todoRequest.name());
  }

  @Test
  @Order(2)
  @DisplayName("팀_Todo_생성")
  @WithCustomMockUser
  void createTeamTodos() throws Exception {
    Team team = Team.from("new team");
    User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);
    team.adduser(user);
    user.joinTeam(team);
    teamRepository.save(team);

    TodoRequest todoRequest = new TodoRequest("todo name", LocalDateTime.now().minusDays(1), Importance.MIDDLE);
    String body = objectMapper.writeValueAsString(todoRequest);

    mockMvc.perform(post(END_POINT + "/" + team.getId())
            .contentType(APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isCreated());

    List<Todo> todo = todoRepository.findAll();
    assertThat(todo.get(0).getTeam()).isNotNull();
  }

  @Test
  @Order(3)
  @DisplayName("Todo_조회")
  @WithCustomMockUser
  void readTodoInfo() throws Exception {
    TodoRequest todoRequest = new TodoRequest("todo name", LocalDateTime.now().minusDays(1), Importance.MIDDLE);
    User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);
    Todo todo = todoRepository.save(Todo.from(todoRequest, user));

    mockMvc.perform(get(END_POINT + "/" + todo.getId()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(todo.getId()))
        .andExpect(jsonPath("$.name").value(todo.getName()))
        .andExpect(jsonPath("$.importance").value(todo.getImportance().name()))
        .andExpect(jsonPath("$.team").value(todo.getTeam()));
  }

  @Test
  @Order(4)
  @DisplayName("Todo_목록_조회")
  @WithCustomMockUser
  void readTodosInfo() throws Exception {
    List<Todo> todos = new ArrayList<>();
    User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);

    for (int i = 0; i < 10; i++) {
      TodoRequest todoRequest = new TodoRequest("todo name" + i, LocalDateTime.now().minusDays(i), Importance.MIDDLE);
      Todo todo = Todo.from(todoRequest, user);
      todos.add(todo);
    }
    todoRepository.saveAll(todos);

    ResultActions actions = mockMvc.perform(get(END_POINT))
        .andDo(print())
        .andExpect(status().isOk());

    for (int i = 0; i < todos.size(); i++) {
      actions.andExpect(jsonPath("$[%d].id".formatted(i)).value(todos.get(i).getId()))
          .andExpect(jsonPath("$[%d].name".formatted(i)).value(todos.get(i).getName()))
          .andExpect(jsonPath("$[%d].importance".formatted(i)).value(todos.get(i).getImportance().name()))
          .andExpect(jsonPath("$[%d].team".formatted(i)).value(todos.get(i).getTeam()));
    }
  }

  @Test
  @Order(5)
  @DisplayName("Todo_수정")
  @WithCustomMockUser
  void updateTodo() throws Exception {
    TodoRequest todoRequest = new TodoRequest("todo name", LocalDateTime.now().minusDays(1), Importance.MIDDLE);
    User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);
    Todo todo = todoRepository.save(Todo.from(todoRequest, user));

    TodoRequest newTodoRequest = new TodoRequest("new todo name", LocalDateTime.now().minusDays(1), Importance.HIGH);
    String body = objectMapper.writeValueAsString(newTodoRequest);

    mockMvc.perform(patch(END_POINT + "/" + todo.getId())
            .contentType(APPLICATION_JSON)
            .content(body))
        .andDo(print())
        .andExpect(status().isNoContent());

    Todo foundTodo = todoRepository.findById(todo.getId()).orElseThrow(NoSuchElementException::new);
    assertThat(foundTodo.getName()).isEqualTo(newTodoRequest.name());
    assertThat(foundTodo.getImportance()).isEqualTo(newTodoRequest.importance());
  }

  @Test
  @Order(6)
  @DisplayName("Todo_완료상태_수정")
  @WithCustomMockUser
  void toggleTodo() throws Exception {
    TodoRequest todoRequest = new TodoRequest("todo name", LocalDateTime.now().minusDays(1), Importance.MIDDLE);
    User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);
    Todo todo = todoRepository.save(Todo.from(todoRequest, user));

    mockMvc.perform(patch(END_POINT + "/" + todo.getId() + "/toggle"))
        .andDo(print())
        .andExpect(status().isNoContent());

    Todo foundTodo = todoRepository.findById(todo.getId()).orElseThrow(NoSuchElementException::new);
    assertThat(foundTodo.isDone()).isTrue();
  }

  @Test
  @Order(7)
  @DisplayName("Todo_삭제")
  @WithCustomMockUser
  void deleteTodo() throws Exception {
    TodoRequest todoRequest = new TodoRequest("todo name", LocalDateTime.now().minusDays(1), Importance.MIDDLE);
    User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);
    Todo todo = todoRepository.save(Todo.from(todoRequest, user));

    mockMvc.perform(delete(END_POINT + "/" + todo.getId()))
        .andDo(print())
        .andExpect(status().isNoContent());

    Optional<Todo> foundTodo = todoRepository.findById(todo.getId());
    assertThatThrownBy(foundTodo::get).isInstanceOf(Exception.class);
  }
}
