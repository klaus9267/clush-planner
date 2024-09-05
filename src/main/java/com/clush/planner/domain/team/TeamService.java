package com.clush.planner.domain.team;

import com.clush.planner.application.handler.error.CustomException;
import com.clush.planner.application.handler.error.ErrorCode;
import com.clush.planner.domain.team.dto.TeamResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamService {
  private final TeamRepository teamRepository;

  @Transactional
  public void createTeam(final String name) {
    teamRepository.findByName(name).ifPresent(team -> {
      throw new CustomException(ErrorCode.CONFLICT_TEAM_NAME);
    });

    final Team team = Team.from(name);
    teamRepository.save(team);
  }

  public TeamResponse readTeamInfo(final Long teamId) {
    final Team team = teamRepository.findById(teamId).orElseThrow(ErrorCode.NOT_FOUND_TEAM);
    return TeamResponse.from(team);
  }

  public Team readTeam(final Long teamId) {
    return teamRepository.findById(teamId).orElseThrow(ErrorCode.NOT_FOUND_TEAM);
  }

  public void deleteTeam(final Team team) {
    teamRepository.delete(team);
  }
}
