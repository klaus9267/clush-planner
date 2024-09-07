package com.clush.planner.domain.todo;

import com.clush.planner.application.auth.SecurityUtil;
import com.clush.planner.application.handler.error.CustomException;
import com.clush.planner.application.handler.error.ErrorCode;
import com.clush.planner.domain.team.Team;
import com.clush.planner.domain.team.TeamService;
import com.clush.planner.domain.todo.dto.TodoRequest;
import com.clush.planner.domain.todo.dto.TodoResponse;
import com.clush.planner.domain.todo.param.TodoCondition;
import com.clush.planner.domain.user.User;
import com.clush.planner.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TodoFacade {
  private final TodoService todoService;
  private final UserService userService;
  private final TeamService teamService;
  private final SecurityUtil securityUtil;

  @Transactional
  public void createTodo(final TodoRequest todoRequest) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final User currentUser = userService.readUser(currentUserId);
    todoService.createTodo(todoRequest, currentUser);
  }

  @Transactional
  public void createTeamTodos(final TodoRequest todoRequest, final long teamId) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final User currentUser = userService.readUser(currentUserId);
    final Team team = teamService.readTeam(teamId);

    if (!currentUser.getTeam().getId().equals(team.getId())) {
      throw new CustomException(ErrorCode.INVALID_TEAM_USER);
    }

    todoService.createTeamTodo(todoRequest, team);
  }

  public TodoResponse readTodoInfo(final long id) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final Todo todo = todoService.readTodo(id, currentUserId);
    return TodoResponse.from(todo);
  }

  public List<TodoResponse> readTodoInfos(final TodoCondition todoCondition) {
    final List<Todo> todos = todoService.readTodos(todoCondition);
    return TodoResponse.from(todos);
  }

  @Transactional
  public void updateTodo(final long id, final TodoRequest todoRequest) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final Todo todo = todoService.readTodo(id, currentUserId);
    todo.updateTodo(todoRequest);
  }

  @Transactional
  public void deleteTodo(final long id) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final Todo todo = todoService.readTodo(id, currentUserId);
    todoService.deleteTodo(todo);
  }
}
