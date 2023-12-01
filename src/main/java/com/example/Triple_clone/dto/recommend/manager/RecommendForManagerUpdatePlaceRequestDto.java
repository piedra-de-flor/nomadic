package com.example.Triple_clone.dto.recommend.manager;

import java.util.Optional;

public record RecommendForManagerUpdatePlaceRequestDto(
        long placeId,
        Optional<String> title,
        Optional<String> notionUrl,
        Optional<String> subTitle,
        Optional<String> location,
        Optional<String> mainImage) {
}