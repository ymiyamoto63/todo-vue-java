package com.example.todo.domain.valueobject;

import java.util.Objects;

public final class ProjectName {

    private static final int MAX_LENGTH = 100;

    private final String value;

    public ProjectName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("プロジェクト名は必須です");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("プロジェクト名は" + MAX_LENGTH + "文字以内で入力してください");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectName)) return false;
        ProjectName other = (ProjectName) o;
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
