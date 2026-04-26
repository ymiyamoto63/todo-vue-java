package com.example.todo.domain.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public final class TodoDescription {

    private static final int MAX_LENGTH = 1000;

    private final String value;

    public TodoDescription(String value) {
        if (value != null && value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("説明は" + MAX_LENGTH + "文字以内で入力してください");
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return value != null ? value : "";
    }
}
