package com.example.Triple_clone.domain.vo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DTYPE {
    CAFE("C"),
    Restaurant("R"),
    Tourism("T");

    private final String dtype;
}
