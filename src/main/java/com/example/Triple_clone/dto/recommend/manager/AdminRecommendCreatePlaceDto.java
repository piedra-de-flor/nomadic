package com.example.Triple_clone.dto.recommend.manager;

import com.example.Triple_clone.entity.Place;
import lombok.NonNull;

public record AdminRecommendCreatePlaceDto(
        @NonNull
        String title,
        @NonNull
        String notionUrl,
        String subTitle,
        @NonNull
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
