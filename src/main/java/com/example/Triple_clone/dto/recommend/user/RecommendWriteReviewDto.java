package com.example.Triple_clone.dto.recommend.user;

import com.example.Triple_clone.entity.Place;
import com.example.Triple_clone.entity.Review;
import com.example.Triple_clone.entity.User;
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
