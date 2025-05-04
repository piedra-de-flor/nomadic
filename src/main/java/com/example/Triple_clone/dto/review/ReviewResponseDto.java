package com.example.Triple_clone.dto.review;

import com.example.Triple_clone.domain.entity.Review;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ReviewResponseDto {
    private Long id;
    private String content;
    private List<ReviewResponseDto> replies;

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.content = review.getContent();
        this.replies = review.getChildren()
                .stream()
                .map(ReviewResponseDto::new)
                .collect(Collectors.toList());
    }
}
