package com.example.todo.domain.specification.todo;

import com.example.todo.domain.entity.Todo;
import com.example.todo.domain.specification.AbstractSpecification;

/**
 * 完了済みTodoのSpecification。
 */
public class CompletedTodoSpecification extends AbstractSpecification<Todo> {

    @Override
    public boolean isSatisfiedBy(Todo todo) {
        return todo.isCompleted();
    }
}
