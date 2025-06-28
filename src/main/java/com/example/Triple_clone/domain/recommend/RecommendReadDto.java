package com.example.Triple_clone.domain.recommend;

import com.example.Triple_clone.domain.plan.Location;

import java.time.LocalDateTime;

public record RecommendReadDto(
    long id,
    String title,
    String notionUrl,
    String subTitle,
    Location location,
    LocalDateTime date,
    boolean like) {

    public RecommendReadDto(Recommendation recommendation, boolean like) {
        this(recommendation.getId(), recommendation.getTitle(), recommendation.getNotionUrl(), recommendation.getSubTitle(), recommendation.getLocation(), recommendation.getDate(), like);
    }
}
