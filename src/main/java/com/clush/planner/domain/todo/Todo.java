package com.clush.planner.domain.todo;

import com.clush.planner.domain.common.Importance;
import com.clush.planner.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "todos")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Todo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(columnDefinition = "boolean default false")
  @Builder.Default
  private boolean isDone = false;

  private LocalDateTime deadline;

  @Column(nullable = false)
  private Importance importance;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;
}
