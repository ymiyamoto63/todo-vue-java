package com.example.todo.service;

import com.example.todo.dto.TodoRequest;
import com.example.todo.dto.TodoResponse;
import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TodoService {
    private final TodoRepository repository;

    public TodoService(TodoRepository repository) {
        this.repository = repository;
    }

    public List<TodoResponse> findAll() {
        return repository.findAll().stream()
                .map(TodoResponse::from)
                .toList();
    }

    public TodoResponse findById(Long id) {
        Todo todo = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Todo not found: " + id));
        return TodoResponse.from(todo);
    }

    public TodoResponse create(TodoRequest request) {
        Todo todo = new Todo(null, request.getTitle(), request.getDescription());
        return TodoResponse.from(repository.save(todo));
    }

    public TodoResponse update(Long id, TodoRequest request) {
        Todo todo = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Todo not found: " + id));
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setCompleted(request.isCompleted());
        todo.setUpdatedAt(LocalDateTime.now());
        return TodoResponse.from(repository.save(todo));
    }

    public TodoResponse toggleComplete(Long id) {
        Todo todo = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Todo not found: " + id));
        todo.setCompleted(!todo.isCompleted());
        todo.setUpdatedAt(LocalDateTime.now());
        return TodoResponse.from(repository.save(todo));
    }

    public void delete(Long id) {
        if (!repository.deleteById(id)) {
            throw new NoSuchElementException("Todo not found: " + id);
        }
    }
}
