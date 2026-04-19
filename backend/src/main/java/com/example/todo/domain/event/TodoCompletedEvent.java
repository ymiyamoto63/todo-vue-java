package com.example.todo.domain.event;

import com.example.todo.domain.valueobject.ProjectId;
import com.example.todo.domain.valueobject.TodoId;
import com.example.todo.domain.valueobject.TodoTitle;

import java.time.LocalDateTime;

public record TodoCompletedEvent(
        ProjectId projectId,
        TodoId todoId,
        TodoTitle todoTitle,
        LocalDateTime occurredAt
) implements DomainEvent {

    public TodoCompletedEvent(ProjectId projectId, TodoId todoId, TodoTitle todoTitle) {
        this(projectId, todoId, todoTitle, LocalDateTime.now());
    }
}
