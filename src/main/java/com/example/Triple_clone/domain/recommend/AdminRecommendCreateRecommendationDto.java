package com.example.Triple_clone.domain.recommend;

import com.example.Triple_clone.domain.plan.Location;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdminRecommendCreateRecommendationDto(
        @NotBlank(message = "Title can not be null")
        String title,
        @NotBlank(message = "Notion URL can not be null")
        String notionUrl,
        String subTitle,
        @NotNull(message = "Location can not be null")
        Location location) {
    public Recommendation toEntity() {
        return Recommendation.builder()
                .location(location)
                .notionUrl(notionUrl)
                .subTitle(subTitle)
                .title(title)
                .build();
    }
}
