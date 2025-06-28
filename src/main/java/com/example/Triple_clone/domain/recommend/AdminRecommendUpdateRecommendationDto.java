package com.example.Triple_clone.domain.recommend;

import com.example.Triple_clone.domain.plan.Location;

public record AdminRecommendUpdateRecommendationDto(
        long placeId,
        String title,
        String notionUrl,
        String subTitle,
        Location location) {
}