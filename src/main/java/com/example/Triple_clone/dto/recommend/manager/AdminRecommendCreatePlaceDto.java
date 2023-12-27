package com.example.Triple_clone.dto.recommend.manager;

import com.example.Triple_clone.entity.Place;
import lombok.NonNull;

import java.util.Objects;

public record AdminRecommendCreatePlaceDto(
        String title,
        String notionUrl,
        String subTitle,
        String location,
        String mainImage) {

    public AdminRecommendCreatePlaceDto {
        Objects.requireNonNull(title);
        Objects.requireNonNull(notionUrl);
        Objects.requireNonNull(location);
    }
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
