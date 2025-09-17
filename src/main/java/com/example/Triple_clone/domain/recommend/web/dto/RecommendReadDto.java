package com.example.Triple_clone.domain.recommend.web.dto;

import com.example.Triple_clone.domain.plan.domain.Location;
import com.example.Triple_clone.domain.recommend.domain.Recommendation;

import java.time.LocalDateTime;
import java.util.Set;

public record RecommendReadDto(
        long id,
        String title,
        String subTitle,
        String author,
        Location location,
        LocalDateTime createdAt,
        String price,
        Set<String> tags,
        int likesCount,
        int reviewsCount,
        int viewsCount,
        boolean like) {

    public RecommendReadDto(Recommendation recommendation, String author, boolean like) {
        this(recommendation.getId(),
                recommendation.getTitle(),
                recommendation.getSubTitle(),
                author,
                recommendation.getLocation(),
                recommendation.getCreatedAt(),
                recommendation.getPrice(),
                recommendation.getTags(),
                recommendation.getLikesCount(),
                recommendation.getReviewsCount(),
                recommendation.getViewsCount(),
                like);
    }
}
