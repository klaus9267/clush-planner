package com.clush.planner.domain.schedule;

import com.clush.planner.domain.common.Importance;
import com.clush.planner.domain.schedule.dto.ScheduleRequest;
import com.clush.planner.domain.team.Team;
import com.clush.planner.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

  @ManyToOne(fetch = FetchType.LAZY)
  private Team team;

  public static Schedule from(final ScheduleRequest scheduleRequest, final User user) {
    return Schedule.builder()
        .name(scheduleRequest.name())
        .date(scheduleRequest.date())
        .startedAt(scheduleRequest.startedAt())
        .endedAt(scheduleRequest.endedAt())
        .importance(scheduleRequest.importance())
        .user(user)
        .build();
  }

  public static List<Schedule> from(final ScheduleRequest scheduleRequest, final Team team, final User currentUser) {
    return team.getUsers().stream()
        .filter(user -> !user.getId().equals(currentUser.getId()))
        .map(user -> Schedule.builder()
            .name(scheduleRequest.name())
            .date(scheduleRequest.date())
            .startedAt(scheduleRequest.startedAt())
            .endedAt(scheduleRequest.endedAt())
            .importance(scheduleRequest.importance())
            .user(user)
            .team(team)
            .build()
        ).toList();

  }

  public void updateSchedule(final ScheduleRequest scheduleRequest) {
    this.name = scheduleRequest.name();
    this.date = scheduleRequest.date();
    this.startedAt = scheduleRequest.startedAt();
    this.endedAt = scheduleRequest.endedAt();
    this.importance = scheduleRequest.importance();
  }
}
