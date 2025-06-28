package com.example.Triple_clone.domain.review;

import com.example.Triple_clone.domain.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByParentIsNullAndRecommendationIdAndDeletedFalseOrderByIdDesc(Long recommendationId, Pageable pageable);

    Page<Review> findByParentIdAndDeletedFalseOrderByIdAsc(Long parentId, Pageable pageable);

}
