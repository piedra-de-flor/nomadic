package com.example.Triple_clone.domain.recommend.web.dto;

import com.example.Triple_clone.domain.plan.domain.Location;
import com.example.Triple_clone.domain.recommend.domain.Recommendation;
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
