package com.example.todo.dto;

import com.example.todo.domain.valueobject.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "TODO作成・更新リクエスト")
public class TodoRequest {
    @Schema(description = "タイトル", example = "買い物をする", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "タイトルは必須です")
    @Size(max = 200, message = "タイトルは200文字以内で入力してください")
    private String title;

    @Schema(description = "説明", example = "牛乳・卵・パンを買う")
    @Size(max = 1000, message = "説明は1000文字以内で入力してください")
    private String description;

    @Schema(description = "締め切り日", example = "2026-04-30")
    private LocalDate dueDate;

    @Schema(description = "優先度", example = "HIGH", allowableValues = {"HIGH", "MEDIUM", "LOW"})
    private Priority priority;

    @Schema(description = "完了フラグ", example = "false")
    private boolean completed;
}
