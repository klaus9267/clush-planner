package com.clush.planner.domain.schedule;

import com.clush.planner.application.handler.error.ErrorCode;
import com.clush.planner.domain.schedule.dto.ScheduleRequest;
import com.clush.planner.domain.schedule.param.ScheduleCondition;
import com.clush.planner.domain.schedule.repository.ScheduleCustomRepository;
import com.clush.planner.domain.schedule.repository.ScheduleRepository;
import com.clush.planner.domain.team.Team;
import com.clush.planner.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
  private final ScheduleRepository scheduleRepository;
  private final ScheduleCustomRepository scheduleCustomRepository;

  public void createSchedule(final ScheduleRequest scheduleRequest, final User user) {
    final Schedule schedule = Schedule.from(scheduleRequest, user);
    scheduleRepository.save(schedule);
  }

  public void createTeamSchedule(final ScheduleRequest scheduleRequest, final Team team) {
    final List<Schedule> schedules = Schedule.from(scheduleRequest, team);
    scheduleRepository.saveAll(schedules);
  }

  public void createSchedules(final List<Schedule> schedules) {
    scheduleRepository.saveAll(schedules);
  }

  public Schedule readSchedule(final long id) {
    return scheduleRepository.findById(id).orElseThrow(ErrorCode.NOT_FOUND_SCHEDULE);
  }

  public List<Schedule> readSchedules(final ScheduleCondition scheduleCondition, final long userId) {
    return scheduleCustomRepository.readSchedules(scheduleCondition, userId);
  }

  public void deleteSchedule(final Schedule schedule) {
    scheduleRepository.delete(schedule);
  }
}
