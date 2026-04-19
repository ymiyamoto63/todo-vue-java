package com.example.todo.application.service;

import com.example.todo.domain.entity.Project;
import com.example.todo.domain.repository.ProjectRepository;
import com.example.todo.domain.valueobject.ProjectId;
import com.example.todo.domain.valueobject.ProjectName;
import com.example.todo.domain.valueobject.TodoDescription;
import com.example.todo.domain.valueobject.TodoId;
import com.example.todo.domain.valueobject.TodoTitle;
import com.example.todo.dto.ProjectRequest;
import com.example.todo.dto.ProjectResponse;
import com.example.todo.dto.TodoRequest;
import com.example.todo.dto.TodoResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProjectApplicationService {

    private final ProjectRepository repository;

    public ProjectApplicationService(ProjectRepository repository) {
        this.repository = repository;
    }

    public List<ProjectResponse> findAll() {
        return repository.findAll().stream()
                .map(ProjectResponse::from)
                .toList();
    }

    public ProjectResponse findById(Long id) {
        return ProjectResponse.from(findProjectById(id));
    }

    public ProjectResponse create(ProjectRequest request) {
        Project project = new Project(new ProjectName(request.getName()));
        return ProjectResponse.from(repository.save(project));
    }

    public void delete(Long id) {
        if (!repository.deleteById(new ProjectId(id))) {
            throw new NoSuchElementException("プロジェクトが見つかりません: " + id);
        }
    }

    public TodoResponse addTodo(Long projectId, TodoRequest request) {
        Project project = findProjectById(projectId);

        // Todo の追加は Project を経由する（集約のルール）
        var todo = project.addTodo(
                new TodoTitle(request.getTitle()),
                new TodoDescription(request.getDescription())
        );
        repository.save(project);
        return TodoResponse.from(todo);
    }

    public void removeTodo(Long projectId, Long todoId) {
        Project project = findProjectById(projectId);
        project.removeTodo(new TodoId(todoId));
        repository.save(project);
    }

    public TodoResponse toggleTodo(Long projectId, Long todoId) {
        Project project = findProjectById(projectId);
        project.toggleTodo(new TodoId(todoId));
        repository.save(project);
        return project.getTodos().stream()
                .filter(t -> new TodoId(todoId).equals(t.getId()))
                .map(TodoResponse::from)
                .findFirst()
                .orElseThrow();
    }

    private Project findProjectById(Long id) {
        return repository.findById(new ProjectId(id))
                .orElseThrow(() -> new NoSuchElementException("プロジェクトが見つかりません: " + id));
    }
}
