package com.example.Triple_clone.domain.recommend.infra;

import com.example.Triple_clone.domain.recommend.domain.RecommendationLike;
import com.example.Triple_clone.domain.recommend.domain.RecommendationLikeId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface RecommendationLikeRepository extends JpaRepository<RecommendationLike, RecommendationLikeId> {

    boolean existsById(RecommendationLikeId id);

    long countByIdRecommendationId(Long recommendationId);

    Page<RecommendationLike> findByIdRecommendationIdOrderByCreatedAtDesc(Long recommendationId, Pageable pageable);
    Page<RecommendationLike> findByIdUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    @Query("select l.id.recommendationId from RecommendationLike l " +
            "where l.id.userId = :userId and l.id.recommendationId in :recIds")
    Set<Long> findLikedRecIds(@Param("userId") Long userId, @Param("recIds") Collection<Long> recIds);

    @Query("select l.id.userId from RecommendationLike l " +
            "where l.id.recommendationId = :recId and l.id.userId in :userIds")
    List<Long> findExistingUserIds(@Param("recId") Long recId, @Param("userIds") Collection<Long> userIds);

    @Modifying
    @Query(value = "DELETE FROM recommendation_like WHERE recommendation_id = :recId AND user_id IN (:userIds)", nativeQuery = true)
    int bulkDelete(@Param("recId") Long recId, @Param("userIds") Collection<Long> userIds);
}
