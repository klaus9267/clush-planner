package com.clush.planner.domain.todo.dto;

import com.clush.planner.domain.common.Importance;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TodoRequest(
    @NotBlank(message = "name is required")
    @Size(min = 2)
    @Schema(example = "Todo 제작")
    String name,
    LocalDateTime deadline,
    Importance importance
) {
}
