package com.clush.planner.domain.todo.dto;

import com.clush.planner.domain.common.Importance;
import com.clush.planner.domain.team.Team;
import com.clush.planner.domain.todo.Todo;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record TodoResponse(
    long id,
    String name,
    boolean isDone,
    LocalDateTime deadline,
    Importance importance,
    String team
) {
  public static TodoResponse from(final Todo todo) {
    final Team team = todo.getTeam();
    return TodoResponse.builder()
        .id(todo.getId())
        .name(todo.getName())
        .isDone(todo.isDone())
        .deadline(todo.getDeadline())
        .importance(todo.getImportance())
        .team(team == null ? null : team.getName())
        .build();
  }

  public static List<TodoResponse> from(final List<Todo> todos) {
    return todos.stream()
        .map(TodoResponse::from)
        .toList();
  }
}
