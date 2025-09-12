package com.example.Triple_clone.domain.recommend.web.dto;

import com.example.Triple_clone.domain.plan.domain.Location;
import com.example.Triple_clone.domain.recommend.domain.PostMeta;

public record AdminRecommendUpdateRecommendationDto(
        long placeId,
        String title,
        String subTitle,
        Location location,
        String price,
        PostMeta postMeta) {
}