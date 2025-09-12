package com.example.Triple_clone.domain.plan.infra;

import com.example.Triple_clone.domain.plan.domain.ChangeType;
import com.example.Triple_clone.domain.plan.domain.PlanChangeHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlanChangeHistoryRepository extends JpaRepository<PlanChangeHistory, Long> {
    Page<PlanChangeHistory> findByPlanIdOrderByCreatedAtDesc(Long planId, Pageable pageable);
    Page<PlanChangeHistory> findByPlanIdAndChangeTypeOrderByCreatedAtDesc(Long planId, ChangeType changeType, Pageable pageable);
    Page<PlanChangeHistory> findByChangedByIdOrderByCreatedAtDesc(Long memberId, Pageable pageable);
    @Query("SELECT pch FROM PlanChangeHistory pch WHERE pch.targetId = :targetId AND pch.targetType = :targetType ORDER BY pch.createdAt DESC")
    Page<PlanChangeHistory> findByTargetIdAndTargetTypeOrderByCreatedAtDesc(@Param("targetId") Long targetId, @Param("targetType") String targetType, Pageable pageable);
}