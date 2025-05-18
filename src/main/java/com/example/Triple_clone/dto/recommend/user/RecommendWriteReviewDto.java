package com.example.Triple_clone.dto.recommend.user;

import com.example.Triple_clone.domain.entity.Recommendation;
import com.example.Triple_clone.domain.entity.Review;
import com.example.Triple_clone.domain.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RecommendWriteReviewDto(
        long placeId,
        @NotBlank(message = "There is no contents")
        @Size(max = 3000, message = "maximum contents size is 3000 words")
        String content,
        Long parentId) {
    public Review toEntity(Member member, Recommendation recommendation, Review parent) {
        Review review = new Review(member, recommendation, this.content);
        if (parent != null) {
            parent.addChildReview(review);
        }
        return review;
    }
}
