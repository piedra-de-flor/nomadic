package com.example.Triple_clone.dto.recommend.user;

import com.example.Triple_clone.domain.entity.Place;
import com.example.Triple_clone.domain.entity.Review;
import com.example.Triple_clone.domain.entity.User;
import jakarta.validation.constraints.NotNull;

public record RecommendWriteReviewDto(
        long userId,
        long placeId,
        @NotNull
        String content,
        String image) {
    public Review toEntity(User user, Place place) {
        return new Review(user, place, this.content, this.image);
    }
}
