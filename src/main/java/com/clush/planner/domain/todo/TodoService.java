package com.clush.planner.domain.todo;

import com.clush.planner.application.handler.error.ErrorCode;
import com.clush.planner.domain.team.Team;
import com.clush.planner.domain.todo.dto.TodoRequest;
import com.clush.planner.domain.todo.param.TodoCondition;
import com.clush.planner.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {
  private final TodoRepository todoRepository;

  public void createTodo(final TodoRequest todoRequest, final User user) {
    final Todo todo = Todo.from(todoRequest, user);
    todoRepository.save(todo);
  }

  public void createTeamTodo(final TodoRequest todoRequest, final Team team) {
    final List<Todo> todos = Todo.from(todoRequest, team);
    todoRepository.saveAll(todos);
  }

  public List<Todo> readTodos(final TodoCondition todoCondition) {
    return null;
  }

  public Todo readTodo(final long id, final long userId) {
    return todoRepository.findByIdAndUserId(id, userId).orElseThrow(ErrorCode.NOT_FOUND_TODO);
  }

  public void deleteTodo(final Todo todo) {
    todoRepository.delete(todo);
  }
}
