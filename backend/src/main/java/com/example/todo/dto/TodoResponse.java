package com.example.todo.dto;

import com.example.todo.domain.entity.Todo;
import com.example.todo.domain.valueobject.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Schema(description = "TODOレスポンス")
public class TodoResponse {
    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "タイトル", example = "買い物をする")
    private String title;

    @Schema(description = "説明", example = "牛乳・卵・パンを買う")
    private String description;

    @Schema(description = "締め切り日", example = "2026-04-30")
    private LocalDate dueDate;

    @Schema(description = "優先度", example = "HIGH")
    private Priority priority;

    @Schema(description = "完了フラグ", example = "false")
    private boolean completed;

    @Schema(description = "期限切れフラグ", example = "false")
    private boolean overdue;

    @Schema(description = "作成日時")
    private LocalDateTime createdAt;

    @Schema(description = "更新日時")
    private LocalDateTime updatedAt;

    public static TodoResponse from(Todo todo) {
        TodoResponse response = new TodoResponse();
        response.id = todo.getId().getValue();
        response.title = todo.getTitle().getValue();
        response.description = todo.getDescription() != null ? todo.getDescription().getValue() : null;
        response.dueDate = todo.getDueDate() != null ? todo.getDueDate().getValue() : null;
        response.priority = todo.getPriority();
        response.completed = todo.isCompleted();
        response.overdue = todo.isOverdue();
        response.createdAt = todo.getCreatedAt();
        response.updatedAt = todo.getUpdatedAt();
        return response;
    }
}
