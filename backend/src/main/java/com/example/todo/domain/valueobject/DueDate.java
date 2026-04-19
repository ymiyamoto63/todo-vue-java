package com.example.todo.domain.valueobject;

import java.time.LocalDate;
import java.util.Objects;

public final class DueDate {

    private final LocalDate value;

    public DueDate(LocalDate value) {
        if (value == null) {
            throw new IllegalArgumentException("締め切り日は必須です");
        }
        this.value = value;
    }

    public boolean isOverdue() {
        return LocalDate.now().isAfter(value);
    }

    public LocalDate getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DueDate)) return false;
        DueDate other = (DueDate) o;
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
