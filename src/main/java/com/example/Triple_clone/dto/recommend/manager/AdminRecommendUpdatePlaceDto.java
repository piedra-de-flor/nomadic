package com.example.Triple_clone.dto.recommend.manager;

public record AdminRecommendUpdatePlaceDto(
        long placeId,
        String title,
        String notionUrl,
        String subTitle,
        String location,
        String mainImage) {
}