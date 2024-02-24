package com.example.Triple_clone.dto.recommend.admin;

public record AdminRecommendUpdateRecommendationDto(
        long placeId,
        String title,
        String notionUrl,
        String subTitle,
        String location,
        String mainImage) {
}