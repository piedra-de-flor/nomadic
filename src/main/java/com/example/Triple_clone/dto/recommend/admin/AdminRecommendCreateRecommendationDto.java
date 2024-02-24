package com.example.Triple_clone.dto.recommend.admin;

import com.example.Triple_clone.domain.entity.Recommendation;
import jakarta.validation.constraints.NotBlank;

public record AdminRecommendCreateRecommendationDto(
        @NotBlank(message = "Title can not be null")
        String title,
        @NotBlank(message = "Notion URL can not be null")
        String notionUrl,
        String subTitle,
        @NotBlank(message = "Location can not be null")
        String location,
        String mainImage) {
    public Recommendation toEntity() {
        return Recommendation.builder()
                .location(location)
                .notionUrl(notionUrl)
                .subTitle(subTitle)
                .title(title)
                .mainImage(mainImage)
                .build();
    }
}
