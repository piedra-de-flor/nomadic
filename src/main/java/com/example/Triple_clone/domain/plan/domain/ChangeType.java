package com.example.Triple_clone.domain.plan.domain;

import lombok.Getter;

@Getter
public enum ChangeType {
    CREATED("생성됨"),
    UPDATED("수정됨"),
    DELETED("삭제됨"),
    MOVED("이동됨"),
    SHARED("공유됨"),
    UNSHARED("공유해제됨");

    private final String description;

    ChangeType(String description) {
        this.description = description;
    }

}