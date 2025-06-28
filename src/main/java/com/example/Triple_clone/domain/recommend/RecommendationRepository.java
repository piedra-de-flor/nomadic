package com.example.Triple_clone.domain.recommend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    Page<Recommendation> findAllByOrderByTitleDesc(Pageable pageable);
    Page<Recommendation> findAllByOrderByDateDesc(Pageable pageable);
}
