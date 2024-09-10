package com.clush.planner.domain.schedule.dto;

import com.clush.planner.domain.common.Importance;
import com.clush.planner.domain.schedule.Schedule;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ScheduleResponse(
    long id,
    String name,
    LocalDate date,
    LocalDateTime startedAt,
    LocalDateTime endedAt,
    Importance importance
) {
  public static ScheduleResponse from(final Schedule schedule) {
    return ScheduleResponse.builder()
        .id(schedule.getId())
        .name(schedule.getName())
        .date(schedule.getDate())
        .startedAt(schedule.getStartedAt())
        .endedAt(schedule.getEndedAt())
        .importance(schedule.getImportance())
        .build();
  }

  public static List<ScheduleResponse> from(final List<Schedule> schedules) {
    return schedules.stream()
        .map(ScheduleResponse::from)
        .toList();
  }
}
