package com.example.Triple_clone.domain.vo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RecommendOrderType {
    name("name"),
    date("date");

    public final String property;
}
