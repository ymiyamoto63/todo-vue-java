package com.example.todo.infrastructure.event;

import com.example.todo.domain.event.AllTodosCompletedEvent;
import com.example.todo.domain.event.TodoCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ProjectEventListener {

    private static final Logger log = LoggerFactory.getLogger(ProjectEventListener.class);

    @EventListener
    public void onTodoCompleted(TodoCompletedEvent event) {
        log.info("[ドメインイベント] Todo完了: projectId={}, todoId={}, title=\"{}\", 発生時刻={}",
                event.projectId().getValue(),
                event.todoId().getValue(),
                event.todoTitle().getValue(),
                event.occurredAt());
    }

    @EventListener
    public void onAllTodosCompleted(AllTodosCompletedEvent event) {
        log.info("[ドメインイベント] 全Todo完了: projectId={}, projectName=\"{}\", 総数={}, 発生時刻={}",
                event.projectId().getValue(),
                event.projectName().getValue(),
                event.totalTodos(),
                event.occurredAt());
    }
}
