package com.example.todo.domain.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
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

    @Override
    public String toString() {
        return value;
    }
}
