package com.clush.planner.domain.todo.param;

import com.clush.planner.domain.common.Importance;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

@Getter
public class TodoCondition {
  @Parameter
  private final boolean isDone;
  @Parameter
  private final LocalDateTime deadline;
  @Parameter
  private final Importance importance;

  @ConstructorProperties({"isDone", "deadline", "importance"})
  public TodoCondition(final Boolean isDone, final LocalDateTime deadline, final Importance importance) {
    this.isDone = isDone;
    this.deadline = deadline == null ? LocalDateTime.now() : deadline;
    this.importance = importance;
  }
}
