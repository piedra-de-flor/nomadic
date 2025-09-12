package com.example.Triple_clone.domain.recommend.infra;

import com.example.Triple_clone.domain.recommend.domain.Recommendation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    Page<Recommendation> findAllByOrderByTitleDesc(Pageable pageable);
    Page<Recommendation> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Recommendation> findAllByOrderByLikesCountDesc(Pageable pageable);
    Page<Recommendation> findAllByOrderByViewsCountDesc(Pageable pageable);
    Page<Recommendation> findAllByOrderByReviewsCountDesc(Pageable pageable);
    
}
