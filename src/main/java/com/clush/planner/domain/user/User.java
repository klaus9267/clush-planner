package com.clush.planner.domain.user;

import com.clush.planner.domain.schedule.Schedule;
import com.clush.planner.domain.team.Team;
import com.clush.planner.domain.todo.Todo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
@AllArgsConstructor
@Builder
@Getter
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String uid;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  private Team team;

  @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
  private final List<Todo> todos = new ArrayList<>();

  @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
  private final List<Schedule> schedules = new ArrayList<>();
}
