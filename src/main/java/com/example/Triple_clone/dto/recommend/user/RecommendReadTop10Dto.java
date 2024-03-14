package com.example.Triple_clone.dto.recommend.user;

import com.example.Triple_clone.domain.vo.Image;

public record RecommendReadTop10Dto(
        long id,
        Image image,
        String Title) {
}
