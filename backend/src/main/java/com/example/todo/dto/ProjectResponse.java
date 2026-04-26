package com.example.todo.dto;

import com.example.todo.domain.entity.Project;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "プロジェクトレスポンス")
public class ProjectResponse {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "プロジェクト名", example = "個人タスク")
    private String name;

    @Schema(description = "TODO一覧")
    private List<TodoResponse> todos;

    public static ProjectResponse from(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.id = project.getId().getValue();
        response.name = project.getName().getValue();
        response.todos = project.getTodos().stream()
                .map(TodoResponse::from)
                .toList();
        return response;
    }
}
