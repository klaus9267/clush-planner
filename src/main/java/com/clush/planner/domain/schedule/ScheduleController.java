package com.clush.planner.domain.schedule;

import com.clush.planner.domain.common.swagger.SwaggerCreated;
import com.clush.planner.domain.common.swagger.SwaggerNoContent;
import com.clush.planner.domain.common.swagger.SwaggerOK;
import com.clush.planner.domain.schedule.dto.ScheduleRequest;
import com.clush.planner.domain.schedule.dto.ScheduleResponse;
import com.clush.planner.domain.schedule.param.ScheduleCondition;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
@Tag(name = "SCHEDULE API")
public class ScheduleController {
  private final ScheduleFacade scheduleFacade;

  @PostMapping
  @SwaggerCreated(summary = "일정 생성 API")
  public void createSchedule(@RequestBody final ScheduleRequest scheduleRequest) {
    scheduleFacade.createSchedule(scheduleRequest);
  }

  @PostMapping("{teamId}")
  @SwaggerCreated(summary = "팀 일정 생성 API")
  public void createTeamSchedule(@PathVariable("teamId") final long teamId,
                                 @RequestBody final ScheduleRequest scheduleRequest) {
    scheduleFacade.createTeamSchedule(teamId, scheduleRequest);
  }

  @PostMapping("share")
  @SwaggerCreated(summary = "일정 공유 API(사용자 선택)")
  public void shareScheduleToUsers(@RequestParam final List<Long> userIds,
                                   @RequestParam final long scheduleId) {
    scheduleFacade.shareScheduleToUsers(userIds, scheduleId);
  }

  @PostMapping("share/team")
  @SwaggerCreated(summary = "일정 공유 API(팀 선택)")
  public void shareScheduleToTeam(@RequestParam final long teamId,
                                  @RequestParam final long scheduleId) {
    scheduleFacade.shareScheduleToTeam(teamId, scheduleId);
  }

  @GetMapping("{id}")
  @SwaggerOK(summary = "일정 조회 API")
  public ScheduleResponse readScheduleInfo(@PathVariable("id") final long id) {
    return scheduleFacade.readScheduleInfo(id);
  }

  @GetMapping
  @SwaggerOK(summary = "일정 목록 조회 API")
  public List<ScheduleResponse> readSchedulesInfo(@ParameterObject final ScheduleCondition scheduleCondition) {
    return scheduleFacade.readSchedulesInfo(scheduleCondition);
  }

  @PatchMapping("{id}")
  @SwaggerNoContent(summary = "일정 수정 API")
  public void updateSchedule(@PathVariable("id") final long id,
                             @RequestParam final ScheduleRequest scheduleRequest) {
    scheduleFacade.updateSchedule(scheduleRequest, id);
  }

  @DeleteMapping("{id}")
  @SwaggerNoContent(summary = "일정 삭제 API")
  public void deleteSchedule(@PathVariable("id") final long id) {
    scheduleFacade.deleteSchedule(id);
  }
}
