package com.example.todo.domain.entity;

import com.example.todo.domain.valueobject.DueDate;
import com.example.todo.domain.valueobject.Priority;
import com.example.todo.domain.valueobject.TodoDescription;
import com.example.todo.domain.valueobject.TodoId;
import com.example.todo.domain.valueobject.TodoTitle;

import java.time.LocalDateTime;
import java.util.Objects;

public class Todo {

    private TodoId id;
    private TodoTitle title;
    private TodoDescription description;
    private DueDate dueDate;
    private Priority priority;
    private boolean completed;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 新規作成用（IDはまだない）
    public Todo(TodoTitle title, TodoDescription description, DueDate dueDate, Priority priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority != null ? priority : Priority.defaultPriority();
        this.completed = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Repository が永続化済みデータを復元するためのコンストラクタ
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

    // --- 同一性の比較（IDで判断）---

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

    // --- Getter のみ（setter は公開しない）---

    public TodoId getId() { return id; }
    public void assignId(TodoId id) { this.id = id; } // Repository専用

    public TodoTitle getTitle() { return title; }
    public TodoDescription getDescription() { return description; }
    public DueDate getDueDate() { return dueDate; }
    public Priority getPriority() { return priority; }
    public boolean isCompleted() { return completed; }
    public boolean isOverdue() { return dueDate != null && !completed && dueDate.isOverdue(); }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
