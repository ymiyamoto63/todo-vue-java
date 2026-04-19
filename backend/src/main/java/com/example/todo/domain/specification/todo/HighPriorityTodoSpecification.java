package com.example.todo.domain.specification.todo;

import com.example.todo.domain.entity.Todo;
import com.example.todo.domain.specification.AbstractSpecification;
import com.example.todo.domain.valueobject.Priority;

/**
 * 優先度HighのTodoのSpecification。
 */
public class HighPriorityTodoSpecification extends AbstractSpecification<Todo> {

    @Override
    public boolean isSatisfiedBy(Todo todo) {
        return Priority.HIGH == todo.getPriority();
    }
}
