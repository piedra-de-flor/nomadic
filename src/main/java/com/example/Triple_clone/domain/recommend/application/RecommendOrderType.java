package com.example.Triple_clone.domain.recommend.application;

import org.springframework.data.domain.Sort;

public enum RecommendOrderType {
    CREATED_DESC("createdAt", Sort.Direction.DESC),
    LIKES_DESC("likesCount", Sort.Direction.DESC),
    VIEWS_DESC("viewsCount", Sort.Direction.DESC),
    REVIEWS_DESC("reviewsCount", Sort.Direction.DESC),
    CREATED_ASC("createdAt", Sort.Direction.ASC),
    LIKES_ASC("likesCount", Sort.Direction.ASC),
    VIEWS_ASC("viewsCount", Sort.Direction.ASC),
    REVIEWS_ASC("reviewsCount", Sort.Direction.ASC);

    private final String field;
    private final Sort.Direction direction;

    RecommendOrderType(String field, Sort.Direction direction) {
        this.field = field;
        this.direction = direction;
    }

    public String getField() {
        return field;
    }

    public Sort.Direction getDirection() {
        return direction;
    }

    public Sort toSort() {
        return Sort.by(direction, field);
    }
}
