package com.clush.planner.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotBlank(message = "uid is required")
    @Size(min = 9, max = 20)
    @Schema(example = "clush2024")
    String uid,
    @NotBlank(message = "password is required")
    @Size(min = 8, max = 20)
    @Schema(example = "clush^^7")
    String password
) {
}
