package com.example.todo.domain.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public final class TodoId {

    private final Long value;

    public TodoId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("IDは1以上の値である必要があります");
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
