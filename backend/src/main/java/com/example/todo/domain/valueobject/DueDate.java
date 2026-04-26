package com.example.todo.domain.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@EqualsAndHashCode
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

    @Override
    public String toString() {
        return value.toString();
    }
}
