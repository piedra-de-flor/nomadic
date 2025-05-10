package com.example.Triple_clone.dto.review;

import com.example.Triple_clone.domain.entity.Review;
import lombok.Getter;

@Getter
public class RootReviewResponseDto {
    private ReviewResponseDto reviewResponseDto;
    private int replyCount;

    public RootReviewResponseDto(Review review) {
        this.reviewResponseDto = new ReviewResponseDto(review);
        this.replyCount = (int) review.getChildren().stream()
                .filter(child -> !child.isDeleted())
                .count();
    }
}
