package com.clush.planner.domain.schedule;

import com.clush.planner.application.auth.SecurityUtil;
import com.clush.planner.application.handler.error.CustomException;
import com.clush.planner.application.handler.error.ErrorCode;
import com.clush.planner.domain.schedule.dto.ScheduleRequest;
import com.clush.planner.domain.schedule.dto.ScheduleResponse;
import com.clush.planner.domain.schedule.param.ScheduleCondition;
import com.clush.planner.domain.team.Team;
import com.clush.planner.domain.team.TeamService;
import com.clush.planner.domain.user.User;
import com.clush.planner.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduleFacade {
  private final ScheduleService scheduleService;
  private final TeamService teamService;
  private final UserService userService;
  private final SecurityUtil securityUtil;

  @Transactional
  public void createSchedule(final ScheduleRequest scheduleRequest) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final User currentUser = userService.readUser(currentUserId);
    scheduleService.createSchedule(scheduleRequest, currentUser);
  }

  @Transactional
  public void createTeamSchedule(final long teamId, final ScheduleRequest scheduleRequest) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final User currentUser = userService.readUser(currentUserId);
    final Team team = teamService.readTeam(teamId);
    if (currentUser.getTeam() == null) {
      throw new CustomException(ErrorCode.EMPTY_TEAM);
    }
    if (!currentUser.getTeam().getId().equals(team.getId())) {
      throw new CustomException(ErrorCode.INVALID_TEAM_USER);
    }
    scheduleService.createTeamSchedule(scheduleRequest, team, currentUser);
  }

  public ScheduleResponse readScheduleInfo(final long id) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final Schedule schedule = scheduleService.readSchedule(id);
    if (!schedule.getUser().getId().equals(currentUserId)) {
      throw new CustomException(ErrorCode.INVALID_USER);
    }
    return ScheduleResponse.from(schedule);
  }

  public List<ScheduleResponse> readSchedulesInfo(final ScheduleCondition scheduleCondition) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final List<Schedule> schedules = scheduleService.readSchedules(scheduleCondition, currentUserId);
    return ScheduleResponse.from(schedules);
  }

  @Transactional
  public void updateSchedule(final ScheduleRequest scheduleRequest, final long id) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final Schedule schedule = scheduleService.readSchedule(id);
    if (!schedule.getUser().getId().equals(currentUserId)) {
      throw new CustomException(ErrorCode.INVALID_USER);
    }
    schedule.updateSchedule(scheduleRequest);
  }

  @Transactional
  public void deleteSchedule(final long id) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final Schedule schedule = scheduleService.readSchedule(id);
    if (!schedule.getUser().getId().equals(currentUserId)) {
      throw new CustomException(ErrorCode.INVALID_USER);
    }
    scheduleService.deleteSchedule(schedule);
  }
}
