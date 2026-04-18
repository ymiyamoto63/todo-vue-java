package com.example.todo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "TODO作成・更新リクエスト")
public class TodoRequest {
    @Schema(description = "タイトル", example = "買い物をする", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "タイトルは必須です")
    @Size(max = 200, message = "タイトルは200文字以内で入力してください")
    private String title;

    @Schema(description = "説明", example = "牛乳・卵・パンを買う")
    @Size(max = 1000, message = "説明は1000文字以内で入力してください")
    private String description;

    @Schema(description = "完了フラグ", example = "false")
    private boolean completed;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
