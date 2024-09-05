package com.clush.planner.domain.team;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
  Optional<Team> findByName(final String name);
}
