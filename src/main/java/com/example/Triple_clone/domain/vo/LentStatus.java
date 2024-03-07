package com.example.Triple_clone.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LentStatus {
    POSSIBLE(true),
    IMPOSSIBLE(false);

    private final boolean status;
}
