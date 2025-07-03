package com.example.Triple_clone.domain.recommend.web.dto;

import com.example.Triple_clone.common.file.Image;

public record RecommendReadTop10Dto(
        long id,
        Image image,
        String Title) {
}
