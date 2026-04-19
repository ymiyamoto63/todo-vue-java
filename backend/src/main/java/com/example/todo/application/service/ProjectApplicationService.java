package com.example.todo.application.service;

import com.example.todo.domain.entity.Project;
import com.example.todo.domain.repository.ProjectRepository;
import com.example.todo.domain.specification.Specification;
import com.example.todo.domain.specification.todo.CompletedTodoSpecification;
import com.example.todo.domain.specification.todo.HighPriorityTodoSpecification;
import com.example.todo.domain.specification.todo.OverdueTodoSpecification;
import com.example.todo.domain.valueobject.DueDate;
import com.example.todo.domain.valueobject.ProjectId;
import com.example.todo.domain.valueobject.ProjectName;
import com.example.todo.domain.valueobject.TodoDescription;
import com.example.todo.domain.valueobject.TodoId;
import com.example.todo.domain.valueobject.TodoTitle;
import com.example.todo.dto.ProjectRequest;
import com.example.todo.dto.ProjectResponse;
import com.example.todo.dto.TodoRequest;
import com.example.todo.dto.TodoResponse;
import com.example.todo.domain.entity.Todo;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProjectApplicationService {

    private final ProjectRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    public ProjectApplicationService(ProjectRepository repository, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
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

        DueDate dueDate = request.getDueDate() != null ? new DueDate(request.getDueDate()) : null;

        // Todo の追加は Project を経由する（集約のルール）
        var todo = project.addTodo(
                new TodoTitle(request.getTitle()),
                new TodoDescription(request.getDescription()),
                dueDate,
                request.getPriority()
        );
        repository.save(project);
        return TodoResponse.from(todo);
    }

    /**
     * Specificationパターンを使ってTodoをフィルタリングする。
     * filter パラメータは "overdue", "completed", "high-priority" またはそれらをカンマ区切りで AND 結合する。
     * 例: "overdue,high-priority" → 期限切れ かつ 優先度High のTodo
     */
    public List<TodoResponse> filterTodos(Long projectId, List<String> filters) {
        Project project = findProjectById(projectId);

        if (filters.isEmpty()) {
            return project.getTodos().stream().map(TodoResponse::from).toList();
        }

        Specification<Todo> spec = buildSpecification(filters);
        return project.filterTodos(spec).stream()
                .map(TodoResponse::from)
                .toList();
    }

    private Specification<Todo> buildSpecification(List<String> filters) {
        Specification<Todo> spec = null;
        for (String filter : filters) {
            Specification<Todo> part = switch (filter.trim().toLowerCase()) {
                case "overdue"       -> new OverdueTodoSpecification();
                case "completed"     -> new CompletedTodoSpecification();
                case "not-completed" -> new CompletedTodoSpecification().not();
                case "high-priority" -> new HighPriorityTodoSpecification();
                default -> throw new IllegalArgumentException("不明なフィルタ: " + filter);
            };
            spec = spec == null ? part : spec.and(part);
        }
        return spec;
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

        // save後にドメインイベントを取り出して発行する
        project.pullDomainEvents().forEach(eventPublisher::publishEvent);

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
