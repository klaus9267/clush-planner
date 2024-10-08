package com.clush.planner.domain.user.dto;

import com.clush.planner.domain.team.Team;
import com.clush.planner.domain.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(
    Long id,
    String uid,
    String name,
    String team,
    String token
) {
  public static UserResponse from(final User user) {
    final Team team = user.getTeam();
    return UserResponse.builder()
        .id(user.getId())
        .uid(user.getUid())
        .name(user.getName())
        .team(team != null ? team.getName() : null)
        .build();
  }

  public static UserResponse from(final User user, final String token) {
    final Team team = user.getTeam();
    return UserResponse.builder()
        .id(user.getId())
        .uid(user.getUid())
        .name(user.getName())
        .team(team != null ? team.getName() : null)
        .token(token)
        .build();
  }

  public static List<UserResponse> from(final List<User> users) {
    return users.stream()
        .map(UserResponse::from)
        .toList();
  }

  public static UserResponse fromWithoutTeam(final User user) {
    return UserResponse.builder()
        .id(user.getId())
        .uid(user.getUid())
        .name(user.getName())
        .build();
  }
}
