package com.example.Triple_clone.service.review;

import com.example.Triple_clone.domain.entity.Recommendation;
import com.example.Triple_clone.domain.entity.Review;
import com.example.Triple_clone.domain.entity.User;
import com.example.Triple_clone.dto.recommend.user.RecommendWriteReviewDto;
import com.example.Triple_clone.service.membership.UserService;
import com.example.Triple_clone.service.recommend.user.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReviewFacadeService {
    private final UserService userService;
    private final ReviewService reviewService;
    private final RecommendService recommendService;

    @Transactional
    public void writeReview(RecommendWriteReviewDto writeReviewRequestDto) {
        Recommendation recommendation = recommendService.findById(writeReviewRequestDto.placeId());
        User user = userService.findById(writeReviewRequestDto.userId());
        Review review = writeReviewRequestDto.toEntity(user, recommendation);

        reviewService.save(review);
        recommendation.addReview(review);
    }
}
