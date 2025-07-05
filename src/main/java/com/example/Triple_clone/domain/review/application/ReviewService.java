package com.example.Triple_clone.domain.review.application;

import com.example.Triple_clone.common.logging.logMessage.ReviewLogMessage;
import com.example.Triple_clone.domain.review.domain.Review;
import com.example.Triple_clone.domain.review.infra.ReviewRepository;
import com.example.Triple_clone.domain.review.web.dto.ReviewResponseDto;
import com.example.Triple_clone.domain.review.web.dto.RootReviewResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                    log.warn(ReviewLogMessage.REVIEW_SEARCH_FAILED.format(reviewId));
                    return new EntityNotFoundException("no review entity");
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
