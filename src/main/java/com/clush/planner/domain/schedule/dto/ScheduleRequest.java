package com.clush.planner.domain.schedule.dto;

import com.clush.planner.domain.common.Importance;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ScheduleRequest(
    String name,
    LocalDate date,
    LocalDateTime startedAt,
    LocalDateTime endedAt,
    Importance importance
) {
}
