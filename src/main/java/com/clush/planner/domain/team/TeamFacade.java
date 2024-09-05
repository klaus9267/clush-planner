package com.clush.planner.domain.team;

import com.clush.planner.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamFacade {
  private final TeamService teamService;
  private final UserService userService;
}
