package com.example.todo.domain.valueobject;

import java.util.Objects;

public final class TodoId {

    private final Long value;

    public TodoId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("IDは1以上の値である必要があります");
        }
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoId)) return false;
        TodoId other = (TodoId) o;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
