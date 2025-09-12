package com.example.Triple_clone.domain.review.web.dto;

import com.example.Triple_clone.domain.review.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RootReviewResponseDto {
    private ReviewResponseDto reviewResponseDto;
    private int replyCount;

    public RootReviewResponseDto(Review review, int replyCount) {
        this.reviewResponseDto = new ReviewResponseDto(review);
        this.replyCount = replyCount;
    }
}
