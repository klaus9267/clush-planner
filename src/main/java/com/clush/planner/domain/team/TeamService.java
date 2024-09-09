package com.clush.planner.domain.team;

import com.clush.planner.application.handler.error.CustomException;
import com.clush.planner.application.handler.error.ErrorCode;
import com.clush.planner.domain.team.dto.TeamResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {
  private final TeamRepository teamRepository;

  public Team createTeam(final String name) {
    teamRepository.findByName(name).ifPresent(team -> {
      throw new CustomException(ErrorCode.CONFLICT_TEAM_NAME);
    });

    final Team team = Team.from(name);
    return teamRepository.save(team);
  }

  public TeamResponse readTeamInfo(final long id) {
    final Team team = teamRepository.findById(id).orElseThrow(ErrorCode.NOT_FOUND_TEAM);
    return TeamResponse.from(team);
  }

  public Team readTeam(final long id) {
    return teamRepository.findById(id).orElseThrow(ErrorCode.NOT_FOUND_TEAM);
  }

  public void deleteTeam(final Team team) {
    teamRepository.delete(team);
  }
}
