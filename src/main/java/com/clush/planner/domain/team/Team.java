package com.clush.planner.domain.team;

import com.clush.planner.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "teams")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Team {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @OneToMany(mappedBy = "team")
  private final List<User> users = new ArrayList<>();

  public static Team from(final String name) {
    return Team.builder()
        .name(name)
        .build();
  }

  public void updateName(final String name) {
    this.name = name;
  }

  public void adduser(final User user) {
    this.users.add(user);
  }

  public void removeUser(final User user) {
    this.users.remove(user);
  }

  @PreRemove
  public void clearUsers() {
    for (User user : this.users) {
      user.leaveTeam();
    }
  }
}
