package com.example.Triple_clone.dto.recommend.admin;

import com.example.Triple_clone.domain.vo.Location;

public record AdminRecommendUpdateRecommendationDto(
        long placeId,
        String title,
        String notionUrl,
        String subTitle,
        Location location,
        String mainImage) {
}