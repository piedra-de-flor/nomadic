package com.example.Triple_clone.dto.recommend.admin;

import com.example.Triple_clone.domain.entity.Place;
import jakarta.validation.constraints.NotBlank;

public record AdminRecommendCreatePlaceDto(
        @NotBlank(message = "Title can not be null")
        String title,
        @NotBlank(message = "Notion URL can not be null")
        String notionUrl,
        String subTitle,
        @NotBlank(message = "Location can not be null")
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
