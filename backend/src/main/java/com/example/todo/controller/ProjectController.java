package com.example.todo.controller;

import com.example.todo.application.service.ProjectApplicationService;
import com.example.todo.dto.ProjectRequest;
import com.example.todo.dto.ProjectResponse;
import com.example.todo.dto.TodoRequest;
import com.example.todo.dto.TodoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Tag(name = "Project", description = "プロジェクト管理API")
@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ProjectController {

    private final ProjectApplicationService service;

    public ProjectController(ProjectApplicationService service) {
        this.service = service;
    }

    @Operation(summary = "プロジェクト一覧取得")
    @GetMapping
    public List<ProjectResponse> getAll() {
        return service.findAll();
    }

    @Operation(summary = "プロジェクト取得")
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getById(
            @Parameter(description = "プロジェクトID") @PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.findById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "プロジェクト作成")
    @PostMapping
    public ResponseEntity<ProjectResponse> create(@Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @Operation(summary = "プロジェクト削除")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "プロジェクトID") @PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "TODO追加", description = "プロジェクトにTODOを追加する")
    @PostMapping("/{id}/todos")
    public ResponseEntity<TodoResponse> addTodo(
            @Parameter(description = "プロジェクトID") @PathVariable Long id,
            @Valid @RequestBody TodoRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.addTodo(id, request));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "TODO削除", description = "プロジェクトからTODOを削除する")
    @DeleteMapping("/{id}/todos/{todoId}")
    public ResponseEntity<Void> removeTodo(
            @Parameter(description = "プロジェクトID") @PathVariable Long id,
            @Parameter(description = "TODO ID") @PathVariable Long todoId) {
        try {
            service.removeTodo(id, todoId);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary = "TODOフィルタリング",
        description = "Specificationパターンでフィルタリング。filter に overdue / completed / not-completed / high-priority をカンマ区切りで指定すると AND 結合"
    )
    @GetMapping("/{id}/todos")
    public ResponseEntity<List<TodoResponse>> filterTodos(
            @Parameter(description = "プロジェクトID") @PathVariable Long id,
            @Parameter(description = "フィルタ条件(カンマ区切り)", example = "overdue,high-priority")
            @RequestParam(required = false) List<String> filter) {
        try {
            return ResponseEntity.ok(service.filterTodos(id, Optional.ofNullable(filter).orElse(List.of())));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "TODO完了トグル", description = "TODOの完了/未完了を切り替える")
    @PatchMapping("/{id}/todos/{todoId}/toggle")
    public ResponseEntity<TodoResponse> toggleTodo(
            @Parameter(description = "プロジェクトID") @PathVariable Long id,
            @Parameter(description = "TODO ID") @PathVariable Long todoId) {
        try {
            return ResponseEntity.ok(service.toggleTodo(id, todoId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
