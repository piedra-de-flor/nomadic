package com.example.Triple_clone.domain.review.web.dto;

import com.example.Triple_clone.domain.review.domain.Review;
import lombok.Getter;

@Getter
public class ReviewResponseDto {
    private Long id;
    private String writer;
    private String content;

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.writer = review.getWriter();
        this.content = review.getContent();
    }
}
