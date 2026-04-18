package com.example.todo.application.service;

import com.example.todo.domain.entity.Todo;
import com.example.todo.domain.repository.TodoRepository;
import com.example.todo.domain.valueobject.TodoDescription;
import com.example.todo.domain.valueobject.TodoId;
import com.example.todo.domain.valueobject.TodoTitle;
import com.example.todo.dto.TodoRequest;
import com.example.todo.dto.TodoResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TodoApplicationService {

    private final TodoRepository repository;

    public TodoApplicationService(TodoRepository repository) {
        this.repository = repository;
    }

    public List<TodoResponse> findAll() {
        return repository.findAll().stream()
                .map(TodoResponse::from)
                .toList();
    }

    public TodoResponse findById(Long id) {
        Todo todo = findTodoById(id);
        return TodoResponse.from(todo);
    }

    public TodoResponse create(TodoRequest request) {
        Todo todo = new Todo(
                new TodoTitle(request.getTitle()),
                new TodoDescription(request.getDescription())
        );
        return TodoResponse.from(repository.save(todo));
    }

    public TodoResponse update(Long id, TodoRequest request) {
        Todo todo = findTodoById(id);
        todo.changeTitle(new TodoTitle(request.getTitle()));
        todo.changeDescription(new TodoDescription(request.getDescription()));
        return TodoResponse.from(repository.save(todo));
    }

    public TodoResponse toggleComplete(Long id) {
        Todo todo = findTodoById(id);
        todo.toggleComplete();
        return TodoResponse.from(repository.save(todo));
    }

    public void delete(Long id) {
        if (!repository.deleteById(new TodoId(id))) {
            throw new NoSuchElementException("Todo not found: " + id);
        }
    }

    private Todo findTodoById(Long id) {
        return repository.findById(new TodoId(id))
                .orElseThrow(() -> new NoSuchElementException("Todo not found: " + id));
    }
}
