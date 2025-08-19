package com.example.Triple_clone.domain.plan.application;

import com.example.Triple_clone.common.logging.logMessage.PlanLogMessage;
import com.example.Triple_clone.domain.plan.domain.PlanShare;
import com.example.Triple_clone.domain.plan.domain.ShareStatus;
import com.example.Triple_clone.domain.plan.infra.PlanShareRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanShareService {
    private final PlanShareRepository repository;

    public PlanShare findById(Long shareId) {
        return repository.findById(shareId)
                .orElseThrow(() -> {
                    log.warn(PlanLogMessage.PLAN_SHARE_SEARCH_FAILED.format(shareId));
                    return new EntityNotFoundException("PlanShare not found");
                });
    }

    public List<PlanShare> findByPlanId(Long planId) {
        return repository.findByPlanId(planId);
    }

    public List<PlanShare> findByMemberId(Long memberId) {
        return repository.findByMemberId(memberId);
    }

    public List<PlanShare> findPendingSharesByMemberId(Long memberId) {
        return repository.findByMemberIdAndStatus(memberId, ShareStatus.PENDING);
    }

    public boolean isAlreadyShared(Long planId, Long memberId) {
        return repository.existsByPlanIdAndMemberId(planId, memberId);
    }

    @Transactional
    public PlanShare save(PlanShare planShare) {
        return repository.save(planShare);
    }

    @Transactional
    public void delete(PlanShare planShare) {
        repository.delete(planShare);
    }

    @Transactional
    public PlanShare acceptShare(Long shareId) {
        PlanShare planShare = findById(shareId);
        planShare.accept();
        return repository.save(planShare);
    }

    @Transactional
    public PlanShare rejectShare(Long shareId) {
        PlanShare planShare = findById(shareId);
        planShare.reject();
        return repository.save(planShare);
    }
}
