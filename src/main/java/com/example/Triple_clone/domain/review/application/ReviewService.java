package com.example.Triple_clone.domain.review.application;

import com.example.Triple_clone.common.logging.logMessage.ReviewLogMessage;
import com.example.Triple_clone.domain.review.domain.Review;
import com.example.Triple_clone.domain.review.domain.ReviewStatus;
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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public Page<RootReviewResponseDto> getRootReviews(Long recommendationId, Pageable pageable) {
        Page<Review> page = repository
                .findByParentIsNullAndRecommendation_IdOrderByIdDesc(
                        recommendationId, pageable);

        List<Long> parentIds = page.getContent().stream()
                .map(Review::getId)
                .toList();

        Map<Long, Integer> replyCountMap = parentIds.isEmpty()
                ? Map.of()
                : repository.countActiveChildrenByParentIds(parentIds).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> ((Long) row[1]).intValue()
                ));

        return page.map(r ->
                new RootReviewResponseDto(
                        r,
                        replyCountMap.getOrDefault(r.getId(), 0)
                )
        );
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> getReplies(Long parentId, Pageable pageable) {
        Page<Review> page = repository.findByParent_IdOrderByIdAsc(parentId, pageable);
        return page.map(ReviewResponseDto::new);
    }
}
