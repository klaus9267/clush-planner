package com.clush.planner.domain.todo.param;

import com.clush.planner.domain.common.Importance;
import io.swagger.v3.oas.annotations.Parameter;

import java.time.LocalDateTime;

public record TodoCondition(
    @Parameter
    Boolean isDone,
    @Parameter
    LocalDateTime deadline,
    @Parameter
    Importance importance,
    @Parameter
    Long teamId
) {
}
