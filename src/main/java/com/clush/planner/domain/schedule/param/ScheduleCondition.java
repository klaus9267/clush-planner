package com.clush.planner.domain.schedule.param;

import com.clush.planner.domain.common.Importance;
import io.swagger.v3.oas.annotations.Parameter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ScheduleCondition(
    @Parameter
    LocalDate date,
    @Parameter
    Importance importance
) {
}
