package com.example.Triple_clone.dto;

import lombok.Getter;

@Getter
public class RecommendForUserLikeRequestDto {
    private final long userId;
    private final long placeId;

    RecommendForUserLikeRequestDto(long placeId, long userId) {
        this.userId = userId;
        this.placeId = placeId;
    }
}
