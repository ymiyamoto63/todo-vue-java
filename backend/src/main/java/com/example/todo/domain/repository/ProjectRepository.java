package com.example.todo.domain.repository;

import com.example.todo.domain.entity.Project;
import com.example.todo.domain.valueobject.ProjectId;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {

    List<Project> findAll();

    Optional<Project> findById(ProjectId id);

    Project save(Project project);

    boolean deleteById(ProjectId id);
}
