package com.example.todo.domain.entity;

import com.example.todo.domain.event.AllTodosCompletedEvent;
import com.example.todo.domain.event.DomainEvent;
import com.example.todo.domain.event.TodoCompletedEvent;
import com.example.todo.domain.specification.Specification;
import com.example.todo.domain.valueobject.DueDate;
import com.example.todo.domain.valueobject.Priority;
import com.example.todo.domain.valueobject.ProjectId;
import com.example.todo.domain.valueobject.ProjectName;
import com.example.todo.domain.valueobject.TodoDescription;
import com.example.todo.domain.valueobject.TodoId;
import com.example.todo.domain.valueobject.TodoTitle;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Project {

    @Getter
    private ProjectId id;
    @Getter
    private ProjectName name;
    private final List<Todo> todos;

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public Project(ProjectName name) {
        this.name = name;
        this.todos = new ArrayList<>();
    }

    public Project(ProjectId id, ProjectName name, List<Todo> todos) {
        this.id = id;
        this.name = name;
        this.todos = new ArrayList<>(todos);
    }

    // --- ドメインロジック ---

    public Todo addTodo(TodoTitle title, TodoDescription description, DueDate dueDate, Priority priority) {
        Todo todo = new Todo(title, description, dueDate, priority);
        todos.add(todo);
        return todo;
    }

    public List<Todo> filterTodos(Specification<Todo> spec) {
        return todos.stream()
                .filter(spec::isSatisfiedBy)
                .toList();
    }

    public void removeTodo(TodoId todoId) {
        boolean removed = todos.removeIf(t -> todoId.equals(t.getId()));
        if (!removed) {
            throw new NoSuchElementException("Todo が見つかりません: " + todoId);
        }
    }

    public void completeTodo(TodoId todoId) {
        Todo todo = findTodo(todoId);
        todo.complete();
        domainEvents.add(new TodoCompletedEvent(id, todo.getId(), todo.getTitle()));

        if (!todos.isEmpty() && todos.stream().allMatch(Todo::isCompleted)) {
            domainEvents.add(new AllTodosCompletedEvent(id, name, todos.size()));
        }
    }

    public void reopenTodo(TodoId todoId) {
        findTodo(todoId).reopen();
    }

    public void toggleTodo(TodoId todoId) {
        Todo todo = findTodo(todoId);
        if (todo.isCompleted()) {
            todo.reopen();
        } else {
            completeTodo(todoId);
        }
    }

    public void changeName(ProjectName newName) {
        this.name = newName;
    }

    public void assignId(ProjectId id) { this.id = id; } // Repository専用

    public List<Todo> getTodos() {
        return Collections.unmodifiableList(todos);
    }

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return events;
    }

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

    private Todo findTodo(TodoId todoId) {
        return todos.stream()
                .filter(t -> todoId.equals(t.getId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Todo が見つかりません: " + todoId));
    }
}
