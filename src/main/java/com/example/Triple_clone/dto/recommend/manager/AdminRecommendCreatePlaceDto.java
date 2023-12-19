package com.example.Triple_clone.dto.recommend.manager;

import com.example.Triple_clone.entity.Place;

public record AdminRecommendCreatePlaceDto(
        String title,
        String notionUrl,
        String subTitle,
        String location,
        String mainImage) {
    public Place toEntity() {
        return Place.builder()
                .location(location)
                .notionUrl(notionUrl)
                .subTitle(subTitle)
                .title(title)
                .mainImage(mainImage)
                .build();
    }
}
