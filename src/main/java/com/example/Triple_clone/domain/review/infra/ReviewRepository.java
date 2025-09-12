package com.example.Triple_clone.domain.review.infra;

import com.example.Triple_clone.domain.review.domain.Review;
import com.example.Triple_clone.domain.review.domain.ReviewStatus;
import com.example.Triple_clone.domain.review.web.dto.RootReviewResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @EntityGraph(attributePaths = {"member"})
    Page<Review> findByParent_IdOrderByIdAsc(Long parentId, Pageable pageable);

    @EntityGraph(attributePaths = {"member"})
    Page<Review> findByParentIsNullAndRecommendation_IdOrderByIdDesc(Long recommendationId, Pageable pageable);

    @Query("""
        select c.parent.id as parentId, count(c) as cnt
        from Review c
        where c.parent.id in :parentIds
        group by c.parent.id
    """)
    List<Object[]> countActiveChildrenByParentIds(@Param("parentIds") List<Long> parentIds);
}
