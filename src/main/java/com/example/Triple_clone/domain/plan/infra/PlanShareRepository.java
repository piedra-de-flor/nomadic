package com.example.Triple_clone.domain.plan.infra;

import com.example.Triple_clone.domain.plan.domain.PlanShare;
import com.example.Triple_clone.domain.plan.domain.ShareStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlanShareRepository extends JpaRepository<PlanShare, Long> {

    List<PlanShare> findByPlanId(Long planId);

    List<PlanShare> findByMemberId(Long memberId);

    Optional<PlanShare> findByPlanIdAndMemberId(Long planId, Long memberId);

    List<PlanShare> findByMemberIdAndStatus(Long memberId, ShareStatus status);

    boolean existsByPlanIdAndMemberId(Long planId, Long memberId);
}