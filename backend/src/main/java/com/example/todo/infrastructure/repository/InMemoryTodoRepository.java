package com.example.todo.infrastructure.repository;

import com.example.todo.domain.entity.Todo;
import com.example.todo.domain.repository.TodoRepository;
import com.example.todo.domain.valueobject.TodoId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryTodoRepository implements TodoRepository {

    private final Map<Long, Todo> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<Todo> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Todo> findById(TodoId id) {
        return Optional.ofNullable(store.get(id.getValue()));
    }

    @Override
    public Todo save(Todo todo) {
        if (todo.getId() == null) {
            todo.assignId(new TodoId(idGenerator.getAndIncrement()));
        }
        store.put(todo.getId().getValue(), todo);
        return todo;
    }

    @Override
    public boolean deleteById(TodoId id) {
        return store.remove(id.getValue()) != null;
    }
}
