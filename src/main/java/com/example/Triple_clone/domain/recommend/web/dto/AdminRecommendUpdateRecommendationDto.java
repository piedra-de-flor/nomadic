package com.example.Triple_clone.domain.recommend.web.dto;

import com.example.Triple_clone.domain.plan.domain.Location;

public record AdminRecommendUpdateRecommendationDto(
        long placeId,
        String title,
        String notionUrl,
        String subTitle,
        Location location) {
}