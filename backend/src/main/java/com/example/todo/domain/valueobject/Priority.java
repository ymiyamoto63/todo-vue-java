package com.example.todo.domain.valueobject;

public enum Priority {
    HIGH,
    MEDIUM,
    LOW;

    public static Priority defaultPriority() {
        return MEDIUM;
    }
}
