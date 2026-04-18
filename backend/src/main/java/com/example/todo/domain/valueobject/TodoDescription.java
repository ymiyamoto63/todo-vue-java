package com.example.todo.domain.valueobject;

import java.util.Objects;

public final class TodoDescription {

    private static final int MAX_LENGTH = 1000;

    private final String value;

    public TodoDescription(String value) {
        if (value != null && value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("説明は" + MAX_LENGTH + "文字以内で入力してください");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoDescription)) return false;
        TodoDescription other = (TodoDescription) o;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value != null ? value : "";
    }
}
