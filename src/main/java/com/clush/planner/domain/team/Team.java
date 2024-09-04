package com.clush.planner.domain.team;

import com.clush.planner.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "teams")
@AllArgsConstructor
@Builder
@Getter
public class Team {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
  private final List<User> users = new ArrayList<>();
}
