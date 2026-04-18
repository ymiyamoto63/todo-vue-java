package com.example.todo.domain.valueobject;

import java.util.Objects;

public final class TodoTitle {

    private static final int MAX_LENGTH = 200;

    private final String value;

    public TodoTitle(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("タイトルは必須です");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("タイトルは" + MAX_LENGTH + "文字以内で入力してください");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoTitle)) return false;
        TodoTitle other = (TodoTitle) o;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
