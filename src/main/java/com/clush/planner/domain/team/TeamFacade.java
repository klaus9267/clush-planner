package com.clush.planner.domain.team;

import com.clush.planner.application.auth.SecurityUtil;
import com.clush.planner.application.handler.error.CustomException;
import com.clush.planner.application.handler.error.ErrorCode;
import com.clush.planner.domain.team.dto.TeamResponse;
import com.clush.planner.domain.user.User;
import com.clush.planner.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TeamFacade {
  private final TeamService teamService;
  private final UserService userService;
  private final SecurityUtil securityUtil;

  @Transactional
  public void createTeam(final String name) {
    teamService.createTeam(name);
  }

  @Transactional
  public void joinTeam(final long id) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final User currentUser = userService.readUser(currentUserId);
    if (currentUser.getTeam() != null) {
      throw new CustomException(ErrorCode.ALREADY_JOIN);
    }
    final Team team = teamService.readTeam(id);
    currentUser.joinTeam(team);
  }

  public TeamResponse readTeamInfo(final long id) {
    return teamService.readTeamInfo(id);
  }

  @Transactional
  public void updateTeam(final Long id, final String name) {
    final User currentUser = securityUtil.getCurrentUser();
    final Team team = teamService.readTeam(id);

    if (!currentUser.getTeam().getId().equals(team.getId())) {
      throw new CustomException(ErrorCode.INVALID_TEAM_USER);
    }

    team.updateName(name);
  }

  @Transactional
  public void leaveTeam() {
    final long currentUserId = securityUtil.getCurrentUserId();
    final User user = userService.readUser(currentUserId);
    if (user.getTeam() == null) {
      throw new CustomException(ErrorCode.EMPTY_TEAM);
    }

    user.leaveTeam();
  }

  @Transactional
  public void deleteTeam(final long id) {
    final User currentUser = securityUtil.getCurrentUser();
    final Team team = teamService.readTeam(id);

    if (currentUser.getTeam() == null) {
      throw new CustomException(ErrorCode.EMPTY_TEAM);
    }
    if (!currentUser.getTeam().getId().equals(team.getId())) {
      throw new CustomException(ErrorCode.INVALID_TEAM_USER);
    }

    team.clearUsers();
    teamService.deleteTeam(team);
  }
}
