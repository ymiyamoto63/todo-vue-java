package com.example.todo.domain.entity;

import com.example.todo.domain.valueobject.ProjectId;
import com.example.todo.domain.valueobject.ProjectName;
import com.example.todo.domain.valueobject.TodoDescription;
import com.example.todo.domain.valueobject.TodoId;
import com.example.todo.domain.valueobject.TodoTitle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Project {

    private ProjectId id;
    private ProjectName name;
    private final List<Todo> todos;

    // 新規作成用
    public Project(ProjectName name) {
        this.name = name;
        this.todos = new ArrayList<>();
    }

    // Repository が復元するためのコンストラクタ
    public Project(ProjectId id, ProjectName name, List<Todo> todos) {
        this.id = id;
        this.name = name;
        this.todos = new ArrayList<>(todos);
    }

    // --- ドメインロジック ---

    // Todo の追加は必ず Project を通す
    public Todo addTodo(TodoTitle title, TodoDescription description) {
        Todo todo = new Todo(title, description);
        todos.add(todo);
        return todo;
    }

    public void removeTodo(TodoId todoId) {
        boolean removed = todos.removeIf(t -> todoId.equals(t.getId()));
        if (!removed) {
            throw new NoSuchElementException("Todo が見つかりません: " + todoId);
        }
    }

    public void completeTodo(TodoId todoId) {
        findTodo(todoId).complete();
    }

    public void reopenTodo(TodoId todoId) {
        findTodo(todoId).reopen();
    }

    public void toggleTodo(TodoId todoId) {
        findTodo(todoId).toggleComplete();
    }

    public void changeName(ProjectName newName) {
        this.name = newName;
    }

    // --- 同一性の比較（IDで判断）---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;
        Project other = (Project) o;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // --- Getter ---

    public ProjectId getId() { return id; }
    public void assignId(ProjectId id) { this.id = id; } // Repository専用

    public ProjectName getName() { return name; }

    // 外部には読み取り専用リストを返す（内部リストを直接渡さない）
    public List<Todo> getTodos() {
        return Collections.unmodifiableList(todos);
    }

    // --- private ---

    private Todo findTodo(TodoId todoId) {
        return todos.stream()
                .filter(t -> todoId.equals(t.getId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Todo が見つかりません: " + todoId));
    }
}
