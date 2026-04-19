package com.example.todo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "プロジェクト作成・更新リクエスト")
public class ProjectRequest {

    @Schema(description = "プロジェクト名", example = "個人タスク", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "プロジェクト名は必須です")
    @Size(max = 100, message = "プロジェクト名は100文字以内で入力してください")
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
