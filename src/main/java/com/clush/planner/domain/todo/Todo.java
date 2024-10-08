package com.clush.planner.domain.todo;

import com.clush.planner.domain.common.Importance;
import com.clush.planner.domain.team.Team;
import com.clush.planner.domain.todo.dto.TodoRequest;
import com.clush.planner.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "todos")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
public class Todo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Builder.Default
  @Column(columnDefinition = "boolean default false")
  private boolean isDone = false;

  @Builder.Default
  @Column(columnDefinition = "boolean default false")
  private boolean isShared = false;

  private LocalDateTime deadline;

  @Column(nullable = false)
  private Importance importance;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  private Team team;

  public static Todo from(final TodoRequest todoRequest, final User user) {
    return Todo.builder()
        .name(todoRequest.name())
        .deadline(todoRequest.deadline() == null ? null : todoRequest.deadline())
        .importance(todoRequest.importance())
        .user(user)
        .build();
  }

  public static Todo from(final TodoRequest todoRequest, final User user, final Team team) {
    return Todo.builder()
        .name(todoRequest.name())
        .deadline(todoRequest.deadline() == null ? null : todoRequest.deadline())
        .importance(todoRequest.importance())
        .user(user)
        .team(team)
        .build();
  }


  public static List<Todo> from(final List<User> users, final Todo todo) {
    return users.stream()
        .map(user -> todo.toBuilder()
            .id(null)
            .user(user)
            .build())
        .toList();
  }

  public static List<Todo> from(final Team team, final Todo todo) {
    return team.getUsers().stream()
        .map(user -> todo.toBuilder()
            .id(null)
            .user(user)
            .team(team)
            .build())
        .toList();
  }

  public static List<Todo> from(final TodoRequest todoRequest, final Team team) {
    return team.getUsers().stream()
        .map(user -> Todo.from(todoRequest, user, team))
        .toList();
  }

  public void updateTodo(final TodoRequest todoRequest) {
    this.name = todoRequest.name();
    this.deadline = todoRequest.deadline();
    this.importance = todoRequest.importance();
  }

  public void toggleDone() {
    this.isDone = !this.isDone;
  }

  public void toggleShared() {
    this.isShared = !this.isShared;
  }
}
