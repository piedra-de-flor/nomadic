package com.example.Triple_clone.domain.recommend.infra;

import com.example.Triple_clone.domain.recommend.domain.RecommendationLike;
import com.example.Triple_clone.domain.recommend.domain.RecommendationLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecommendationLikeRepository extends JpaRepository<RecommendationLike, RecommendationLikeId> {
    List<RecommendationLike> findByRecommendationId(Long recommendationId);
    boolean existsByRecommendation_IdAndId_UserId(Long recommendationId, Long userId);
    void deleteByRecommendation_IdAndId_UserId(Long recommendationId, Long userId);
}
