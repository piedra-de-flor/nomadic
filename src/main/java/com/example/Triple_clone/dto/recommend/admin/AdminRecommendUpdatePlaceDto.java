package com.example.Triple_clone.dto.recommend.admin;

public record AdminRecommendUpdatePlaceDto(
        long placeId,
        String title,
        String notionUrl,
        String subTitle,
        String location,
        String mainImage) {
}