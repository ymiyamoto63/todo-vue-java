package com.example.todo.domain.event;

import com.example.todo.domain.valueobject.ProjectId;
import com.example.todo.domain.valueobject.ProjectName;

import java.time.LocalDateTime;

public record AllTodosCompletedEvent(
        ProjectId projectId,
        ProjectName projectName,
        int totalTodos,
        LocalDateTime occurredAt
) implements DomainEvent {

    public AllTodosCompletedEvent(ProjectId projectId, ProjectName projectName, int totalTodos) {
        this(projectId, projectName, totalTodos, LocalDateTime.now());
    }
}
