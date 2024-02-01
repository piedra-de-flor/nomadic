package com.example.Triple_clone.dto.recommend.user;

import com.example.Triple_clone.domain.entity.Place;

import java.time.LocalDateTime;

public record RecommendReadDto(
    long id,
    String title,
    String notionUrl,
    String subTitle,
    String location,
    String mainImage,
    LocalDateTime date,
    boolean like) {

    public RecommendReadDto(Place place, boolean like) {
        this(place.getId(), place.getTitle(), place.getNotionUrl(), place.getSubTitle(), place.getLocation(), place.getMainImage(), place.getDate(), like);
    }
}
