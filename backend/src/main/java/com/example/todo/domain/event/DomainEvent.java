package com.example.todo.domain.event;

import java.time.LocalDateTime;

public interface DomainEvent {
    LocalDateTime occurredAt();
}
