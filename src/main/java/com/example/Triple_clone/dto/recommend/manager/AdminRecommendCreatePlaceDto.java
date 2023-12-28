package com.example.Triple_clone.dto.recommend.manager;

import com.example.Triple_clone.entity.Place;
import jakarta.validation.constraints.NotNull;

public record AdminRecommendCreatePlaceDto(
        @NotNull
        String title,
        @NotNull
        String notionUrl,
        String subTitle,
        @NotNull
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
