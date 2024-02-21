package com.example.Triple_clone.dto.recommend.user;

import com.example.Triple_clone.domain.entity.Place;
import com.example.Triple_clone.domain.entity.Review;
import com.example.Triple_clone.domain.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RecommendWriteReviewDto(
        long userId,
        long placeId,
        @NotBlank(message = "There is no contents")
        @Size(max = 3000, message = "maximum contents size is 3000 words")
        String content,
        String image) {
    public Review toEntity(User user, Place place) {
        return new Review(user, place, this.content, this.image);
    }
}
