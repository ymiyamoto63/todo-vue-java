package com.example.todo.infrastructure.repository;

import com.example.todo.domain.entity.Project;
import com.example.todo.domain.repository.ProjectRepository;
import com.example.todo.domain.valueobject.ProjectId;
import com.example.todo.domain.valueobject.TodoId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryProjectRepository implements ProjectRepository {

    private final Map<Long, Project> store = new ConcurrentHashMap<>();
    private final AtomicLong projectIdGenerator = new AtomicLong(1);
    private final AtomicLong todoIdGenerator = new AtomicLong(1);

    @Override
    public List<Project> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Project> findById(ProjectId id) {
        return Optional.ofNullable(store.get(id.getValue()));
    }

    @Override
    public Project save(Project project) {
        if (project.getId() == null) {
            project.assignId(new ProjectId(projectIdGenerator.getAndIncrement()));
        }
        // IDのない Todo に連番を割り当てる
        project.getTodos().forEach(todo -> {
            if (todo.getId() == null) {
                todo.assignId(new TodoId(todoIdGenerator.getAndIncrement()));
            }
        });
        store.put(project.getId().getValue(), project);
        return project;
    }

    @Override
    public boolean deleteById(ProjectId id) {
        return store.remove(id.getValue()) != null;
    }
}
