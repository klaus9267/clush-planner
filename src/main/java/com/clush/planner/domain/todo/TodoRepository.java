package com.clush.planner.domain.todo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
  Optional<Todo> findByIdAndUserId(final long todoId, final long userID);
}
