package com.example.todo.domain.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
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

    @Override
    public String toString() {
        return value;
    }
}
