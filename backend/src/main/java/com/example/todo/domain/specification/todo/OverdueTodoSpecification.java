package com.example.todo.domain.specification.todo;

import com.example.todo.domain.entity.Todo;
import com.example.todo.domain.specification.AbstractSpecification;

/**
 * 期限切れTodoのSpecification。
 * 未完了かつ締め切り日が過ぎているTodoを対象とする。
 */
public class OverdueTodoSpecification extends AbstractSpecification<Todo> {

    @Override
    public boolean isSatisfiedBy(Todo todo) {
        return todo.isOverdue();
    }
}
