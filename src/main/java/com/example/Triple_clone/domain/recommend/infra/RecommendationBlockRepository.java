package com.example.Triple_clone.domain.recommend.infra;

import com.example.Triple_clone.domain.recommend.domain.RecommendationBlock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationBlockRepository extends JpaRepository<RecommendationBlock, Long> {
}
