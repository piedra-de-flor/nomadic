package com.example.Triple_clone.domain.recommend.web.dto;

import com.example.Triple_clone.domain.plan.domain.Location;

public record RecommendationUpdateDto(
        long placeId,
        String title,
        String subTitle,
        Location location,
        String price,
        String tags
) {
}