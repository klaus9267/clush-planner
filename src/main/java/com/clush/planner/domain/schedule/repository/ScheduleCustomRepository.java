package com.clush.planner.domain.schedule.repository;

import com.clush.planner.domain.schedule.Schedule;
import com.clush.planner.domain.schedule.param.ScheduleCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.clush.planner.domain.schedule.QSchedule.schedule;
import static com.clush.planner.domain.todo.QTodo.todo;

@Repository
@RequiredArgsConstructor
public class ScheduleCustomRepository {
  private final JPAQueryFactory jpaQueryFactory;

  public List<Schedule> readSchedules(final ScheduleCondition scheduleCondition, final long userId) {
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(todo.user.id.eq(userId));

    Optional.ofNullable(scheduleCondition.date())
        .ifPresent(schedule.date::eq);
    Optional.ofNullable(scheduleCondition.importance())
        .ifPresent(schedule.importance::eq);

    return jpaQueryFactory.selectFrom(schedule)
        .where(builder)
        .fetch();
  }
}
