package com.clush.planner.domain.todo;

import com.clush.planner.domain.common.swagger.SwaggerCreated;
import com.clush.planner.domain.common.swagger.SwaggerNoContent;
import com.clush.planner.domain.common.swagger.SwaggerOK;
import com.clush.planner.domain.todo.dto.TodoRequest;
import com.clush.planner.domain.todo.dto.TodoResponse;
import com.clush.planner.domain.todo.param.TodoCondition;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@Tag(name = "Todo API")
public class TodoController {
  private final TodoFacade todoFacade;

  @PostMapping
  @SwaggerCreated(summary = "Todo 생성 API")
  public void createTodo(@RequestBody @Valid final TodoRequest todoRequest) {
    todoFacade.createTodo(todoRequest);
  }

  @PostMapping("{teamId}")
  @SwaggerCreated(summary = "팀 Todo 생성 API")
  public void createTeamTodos(@PathVariable("teamId") final long teamId, @RequestBody @Valid final TodoRequest todoRequest) {
    todoFacade.createTeamTodos(todoRequest, teamId);
  }

  @GetMapping("{id}")
  @SwaggerOK(summary = "Todo 조회 API")
  public TodoResponse readTodoInfo(@PathVariable("id") final long id) {
    return todoFacade.readTodoInfo(id);
  }

  @GetMapping
  @SwaggerOK(summary = "Todo 목록 조회 API")
  public List<TodoResponse> readTodosInfo(@ParameterObject final TodoCondition todoCondition) {
    return todoFacade.readTodosInfo(todoCondition);
  }

  @PatchMapping("{id}")
  @SwaggerNoContent(summary = "Todo 수정 API")
  public void updateTodo(@PathVariable("id") final long id, @RequestBody @Valid final TodoRequest todoRequest) {
    todoFacade.updateTodo(id, todoRequest);
  }

  @PatchMapping("{id}/toggle")
  @SwaggerNoContent(summary = "Todo 완료상태 수정 API")
  public void toggleTodo(@PathVariable("id") final long id) {
    todoFacade.toggleTodo(id);
  }

  @DeleteMapping("{id}")
  @SwaggerNoContent(summary = "Todo 삭제 API")
  public void deleteTodo(@PathVariable("id") final long id) {
    todoFacade.deleteTodo(id);
  }
}
