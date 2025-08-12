package com.example.Triple_clone.domain.plan.domain;

import lombok.Getter;

@Getter
public enum ShareStatus {
    PENDING("대기중"),
    ACCEPTED("수락됨"),
    REJECTED("거부됨"),
    CANCELLED("취소됨");

    private final String description;

    ShareStatus(String description) {
        this.description = description;
    }
}