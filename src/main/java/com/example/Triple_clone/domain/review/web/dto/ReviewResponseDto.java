package com.example.Triple_clone.domain.review.web.dto;

import com.example.Triple_clone.domain.review.domain.Review;
import lombok.Getter;

@Getter
public class ReviewResponseDto {
    private Long id;
    private Long writerId;
    private String writer;
    private String content;
    private String status;

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.writerId = review.getMember().getId();
        this.writer = review.getWriter();
        this.content = review.getContent();
        this.status = review.getStatus().name();
    }
}
