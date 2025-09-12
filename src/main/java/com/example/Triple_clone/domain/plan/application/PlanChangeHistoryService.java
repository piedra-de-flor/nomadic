package com.example.Triple_clone.domain.plan.application;

import com.example.Triple_clone.domain.plan.domain.ChangeType;
import com.example.Triple_clone.domain.plan.domain.PlanChangeHistory;
import com.example.Triple_clone.domain.plan.infra.PlanChangeHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanChangeHistoryService {
    private final PlanChangeHistoryRepository repository;

    @Transactional
    public PlanChangeHistory save(PlanChangeHistory history) {
        return repository.save(history);
    }

    public Page<PlanChangeHistory> findByPlanId(Long planId, Pageable pageable) {
        return repository.findByPlanIdOrderByCreatedAtDesc(planId, pageable);
    }

    public Page<PlanChangeHistory> findByPlanIdAndChangeType(Long planId, ChangeType changeType, Pageable pageable) {
        return repository.findByPlanIdAndChangeTypeOrderByCreatedAtDesc(planId, changeType, pageable);
    }

    public Page<PlanChangeHistory> findByMemberId(Long memberId, Pageable pageable) {
        return repository.findByChangedByIdOrderByCreatedAtDesc(memberId, pageable);
    }

    public Page<PlanChangeHistory> findByTargetIdAndType(Long targetId, String targetType, Pageable pageable) {
        return repository.findByTargetIdAndTargetTypeOrderByCreatedAtDesc(targetId, targetType, pageable);
    }
}