package com.example.todo.domain.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public final class ProjectId {

    private final Long value;

    public ProjectId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("プロジェクトIDは1以上の値である必要があります");
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
