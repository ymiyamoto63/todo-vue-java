package com.example.todo.controller;

import com.example.todo.dto.TodoRequest;
import com.example.todo.model.Todo;
import com.example.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Tag(name = "Todo", description = "TODO管理API")
@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*")
public class TodoController {
    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @Operation(summary = "TODO一覧取得", description = "登録されている全TODOを取得する")
    @ApiResponse(responseCode = "200", description = "取得成功")
    @GetMapping
    public List<Todo> getAll() {
        return service.findAll();
    }

    @Operation(summary = "TODO取得", description = "指定IDのTODOを取得する")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "取得成功"),
        @ApiResponse(responseCode = "404", description = "TODOが存在しない"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getById(
            @Parameter(description = "TODO ID", required = true) @PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.findById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "TODO作成", description = "新しいTODOを作成する")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "作成成功"),
        @ApiResponse(responseCode = "400", description = "バリデーションエラー"),
    })
    @PostMapping
    public ResponseEntity<Todo> create(@Valid @RequestBody TodoRequest request) {
        Todo created = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "TODO更新", description = "指定IDのTODOを更新する")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "更新成功"),
        @ApiResponse(responseCode = "400", description = "バリデーションエラー"),
        @ApiResponse(responseCode = "404", description = "TODOが存在しない"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Todo> update(
            @Parameter(description = "TODO ID", required = true) @PathVariable Long id,
            @Valid @RequestBody TodoRequest request) {
        try {
            return ResponseEntity.ok(service.update(id, request));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "完了状態トグル", description = "指定IDのTODOの完了/未完了を切り替える")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "更新成功"),
        @ApiResponse(responseCode = "404", description = "TODOが存在しない"),
    })
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Todo> toggleComplete(
            @Parameter(description = "TODO ID", required = true) @PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.toggleComplete(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "TODO削除", description = "指定IDのTODOを削除する")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "削除成功"),
        @ApiResponse(responseCode = "404", description = "TODOが存在しない"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "TODO ID", required = true) @PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
