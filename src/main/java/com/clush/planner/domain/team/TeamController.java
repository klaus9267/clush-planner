package com.clush.planner.domain.team;

import com.clush.planner.domain.common.swagger.SwaggerCreatedWithBody;
import com.clush.planner.domain.common.swagger.SwaggerNoContent;
import com.clush.planner.domain.common.swagger.SwaggerOK;
import com.clush.planner.domain.team.dto.TeamResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@Tag(name = "TEAM API")
public class TeamController {
  private final TeamFacade teamFacade;

  @PostMapping("{name}")
  @SwaggerCreatedWithBody(summary = "팀 생성 API")
  public TeamResponse createTeam(@PathVariable("name") final String name) {
    return teamFacade.createTeam(name);
  }

  @PostMapping("join/{id}")
  @SwaggerNoContent(summary = "팀 참가 API")
  public void joinTeam(@PathVariable("id") final long id) {
    teamFacade.joinTeam(id);
  }

  @GetMapping("{id}")
  @SwaggerOK(summary = "팀 정보 조회 API")
  public TeamResponse readTeamInfo(@PathVariable("id") final Long id) {
    return teamFacade.readTeamInfo(id);
  }

  @PatchMapping
  @SwaggerNoContent(summary = "팀 정보 수정 API")
  public void updateTeam(@RequestParam("id") final long id,
                         @RequestParam("name") final String name) {
    teamFacade.updateTeam(id, name);
  }

  @DeleteMapping("{id}")
  @SwaggerNoContent(summary = "팀 삭제 API")
  public void deleteTeam(@PathVariable("id") final Long id) {
    teamFacade.deleteTeam(id);
  }

  @DeleteMapping("leave")
  @SwaggerNoContent(summary = "팀 탈퇴 API")
  public void leaveTeam() {
    teamFacade.leaveTeam();
  }
}
