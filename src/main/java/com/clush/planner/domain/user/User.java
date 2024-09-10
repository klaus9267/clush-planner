package com.clush.planner.domain.user;

import com.clush.planner.domain.schedule.Schedule;
import com.clush.planner.domain.team.Team;
import com.clush.planner.domain.todo.Todo;
import com.clush.planner.domain.user.dto.JoinRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor
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

  public static User from(final JoinRequest joinRequest, final PasswordEncoder passwordEncoder) {
    return User.builder()
        .uid(joinRequest.uid())
        .name(joinRequest.name())
        .password(passwordEncoder.encode(joinRequest.password()))
        .build();
  }

  public void updateName(final String name) {
    this.name = name;
  }

  public void joinTeam(final Team team) {
    this.team = team;
  }

  public void leaveTeam() {
    this.team = null;
  }
}
