package com.example.todo.service;

import com.example.todo.dto.TodoRequest;
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

    public List<Todo> findAll() {
        return repository.findAll();
    }

    public Todo findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Todo not found: " + id));
    }

    public Todo create(TodoRequest request) {
        Todo todo = new Todo(null, request.getTitle(), request.getDescription());
        return repository.save(todo);
    }

    public Todo update(Long id, TodoRequest request) {
        Todo todo = findById(id);
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setCompleted(request.isCompleted());
        todo.setUpdatedAt(LocalDateTime.now());
        return repository.save(todo);
    }

    public Todo toggleComplete(Long id) {
        Todo todo = findById(id);
        todo.setCompleted(!todo.isCompleted());
        todo.setUpdatedAt(LocalDateTime.now());
        return repository.save(todo);
    }

    public void delete(Long id) {
        if (!repository.deleteById(id)) {
            throw new NoSuchElementException("Todo not found: " + id);
        }
    }
}
