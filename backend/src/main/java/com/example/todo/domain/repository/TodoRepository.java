package com.example.todo.domain.repository;

import com.example.todo.domain.entity.Todo;
import com.example.todo.domain.valueobject.TodoId;

import java.util.List;
import java.util.Optional;

public interface TodoRepository {

    List<Todo> findAll();

    Optional<Todo> findById(TodoId id);

    Todo save(Todo todo);

    boolean deleteById(TodoId id);
}
