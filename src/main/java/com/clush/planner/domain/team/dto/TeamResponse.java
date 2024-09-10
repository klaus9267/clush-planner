package com.clush.planner.domain.team.dto;

import com.clush.planner.domain.team.Team;
import com.clush.planner.domain.user.dto.UserResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record TeamResponse(
    Long id,
    String name,
    List<UserResponse> members
) {
  public static TeamResponse from(final Team team) {
    List<UserResponse> userResponses = team.getUsers().stream()
        .map(UserResponse::fromWithoutTeam)
        .toList();

    return TeamResponse.builder()
        .id(team.getId())
        .name(team.getName())
        .members(userResponses)
        .build();
  }
}
