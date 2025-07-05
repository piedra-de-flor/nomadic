package com.example.Triple_clone.domain.review.web.dto;

import com.example.Triple_clone.domain.review.domain.Review;
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
