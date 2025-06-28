package com.example.Triple_clone.domain.recommend;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RecommendOrderType {
    title("title"),
    date("date");

    public final String property;
}
