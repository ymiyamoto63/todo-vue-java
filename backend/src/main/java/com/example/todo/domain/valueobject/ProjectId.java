package com.example.todo.domain.valueobject;

import java.util.Objects;

public final class ProjectId {

    private final Long value;

    public ProjectId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("プロジェクトIDは1以上の値である必要があります");
        }
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectId)) return false;
        ProjectId other = (ProjectId) o;
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
