package com.example.Triple_clone.domain.vo;

public enum RecommendOrderType {
    name("title"),
    date("date");

    public final String property;

    RecommendOrderType(String property) {
        this.property = property;
    }
}
