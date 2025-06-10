package com.example.Triple_clone.service.review;

import com.example.Triple_clone.domain.entity.Review;
import com.example.Triple_clone.dto.review.ReviewResponseDto;
import com.example.Triple_clone.dto.review.RootReviewResponseDto;
import com.example.Triple_clone.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository repository;

    public void save(Review review) {
        repository.save(review);
    }

    @Transactional
    public void delete(Review review) {
        review.softDelete();
    }

    @Transactional
    public void update(Review review, String content) {
        review.update(content);
    }

    public Review findById(Long reviewId) {
        return repository.findById(reviewId)
                .orElseThrow(() -> {
                    log.warn("⚠️ 리뷰 조회 실패 - 존재하지 않는 리뷰: {}", reviewId);
                    return new NoSuchElementException("no review entity");
                });
    }

    public Page<RootReviewResponseDto> getRootReviews(Long recommendationId, Pageable pageable) {
        return repository
                .findByParentIsNullAndRecommendationIdAndDeletedFalseOrderByIdDesc(recommendationId, pageable)
                .map(RootReviewResponseDto::new);
    }

    public Page<ReviewResponseDto> getReplies(Long parentId, Pageable pageable) {
        return repository
                .findByParentIdAndDeletedFalseOrderByIdAsc(parentId, pageable)
                .map(ReviewResponseDto::new);
    }
}
