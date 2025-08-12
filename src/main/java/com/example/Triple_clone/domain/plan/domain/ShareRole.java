package com.example.Triple_clone.domain.plan.domain;

import lombok.Getter;

@Getter
public enum ShareRole {
    OWNER("소유자"),
    EDITOR("편집자"),
    VIEWER("조회자");

    private final String description;

    ShareRole(String description) {
        this.description = description;
    }
}