package com.example.todo.domain.entity;

import com.example.todo.domain.valueobject.DueDate;
import com.example.todo.domain.valueobject.Priority;
import com.example.todo.domain.valueobject.TodoDescription;
import com.example.todo.domain.valueobject.TodoId;
import com.example.todo.domain.valueobject.TodoTitle;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Todo {

    private TodoId id;
    private TodoTitle title;
    private TodoDescription description;
    private DueDate dueDate;
    private Priority priority;
    private boolean completed;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Todo(TodoTitle title, TodoDescription description, DueDate dueDate, Priority priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority != null ? priority : Priority.defaultPriority();
        this.completed = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Todo(TodoId id, TodoTitle title, TodoDescription description, DueDate dueDate,
                Priority priority, boolean completed, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority != null ? priority : Priority.defaultPriority();
        this.completed = completed;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // --- ドメインロジック ---

    public void changeTitle(TodoTitle newTitle) {
        this.title = newTitle;
        this.updatedAt = LocalDateTime.now();
    }

    public void changeDescription(TodoDescription newDescription) {
        this.description = newDescription;
        this.updatedAt = LocalDateTime.now();
    }

    public void complete() {
        if (this.completed) {
            throw new IllegalStateException("すでに完了済みです");
        }
        this.completed = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void reopen() {
        if (!this.completed) {
            throw new IllegalStateException("すでに未完了です");
        }
        this.completed = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void toggleComplete() {
        if (this.completed) {
            reopen();
        } else {
            complete();
        }
    }

    public boolean isOverdue() { return dueDate != null && !completed && dueDate.isOverdue(); }

    public void assignId(TodoId id) { this.id = id; } // Repository専用

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Todo)) return false;
        Todo other = (Todo) o;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
