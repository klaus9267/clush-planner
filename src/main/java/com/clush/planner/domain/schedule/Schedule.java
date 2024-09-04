package com.clush.planner.domain.schedule;

import com.clush.planner.domain.common.Importance;
import com.clush.planner.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "schedules")
@AllArgsConstructor
@Builder
@Getter
public class Schedule {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  private LocalDate date;

  private LocalDateTime start;

  private LocalDateTime end;

  @Column(nullable = false)
  private Importance importance;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;
}
