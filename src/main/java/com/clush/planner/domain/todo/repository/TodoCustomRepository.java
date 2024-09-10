package com.clush.planner.domain.todo.repository;

import com.clush.planner.domain.todo.Todo;
import com.clush.planner.domain.todo.param.TodoCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.clush.planner.domain.todo.QTodo.todo;

@Repository
@RequiredArgsConstructor
public class TodoCustomRepository {
  private final JPAQueryFactory jpaQueryFactory;

  public List<Todo> readTodos(final TodoCondition todoCondition, final long userId) {
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(todo.user.id.eq(userId));

    Optional.ofNullable(todoCondition.isDone())
        .ifPresent(isDone -> builder.and(todo.isDone.eq(todoCondition.isDone())));
    Optional.ofNullable(todoCondition.deadline())
        .ifPresent(deadline -> builder.and(todo.deadline.before(todoCondition.deadline())));
    Optional.ofNullable(todoCondition.importance())
        .ifPresent(importance -> builder.and(todo.importance.eq(todoCondition.importance())));
    Optional.ofNullable(todoCondition.teamId())
        .ifPresent(teamId -> builder.and(todo.team.id.eq(todoCondition.teamId())));

    return jpaQueryFactory.selectFrom(todo)
        .where(builder)
        .fetch();
  }
}
