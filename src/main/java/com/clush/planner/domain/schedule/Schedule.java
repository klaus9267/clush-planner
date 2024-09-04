package com.clush.planner.domain.schedule;

import com.clush.planner.domain.common.Importance;
import com.clush.planner.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "schedules")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Schedule {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  private LocalDate date;

  private LocalDateTime startedAt;

  private LocalDateTime endedAt;

  @Column(nullable = false)
  private Importance importance;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;
}
