package com.example.todo.dto;

import com.example.todo.model.Todo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "TODOレスポンス")
public class TodoResponse {
    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "タイトル", example = "買い物をする")
    private String title;

    @Schema(description = "説明", example = "牛乳・卵・パンを買う")
    private String description;

    @Schema(description = "完了フラグ", example = "false")
    private boolean completed;

    @Schema(description = "作成日時")
    private LocalDateTime createdAt;

    @Schema(description = "更新日時")
    private LocalDateTime updatedAt;

    public static TodoResponse from(Todo todo) {
        TodoResponse response = new TodoResponse();
        response.id = todo.getId();
        response.title = todo.getTitle();
        response.description = todo.getDescription();
        response.completed = todo.isCompleted();
        response.createdAt = todo.getCreatedAt();
        response.updatedAt = todo.getUpdatedAt();
        return response;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isCompleted() { return completed; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
