package com.example.Triple_clone.vo;

public enum RecommendOrderType {
    name("title"),
    date("date");

    public final String property;

    RecommendOrderType(String property) {
        this.property = property;
    }
}
