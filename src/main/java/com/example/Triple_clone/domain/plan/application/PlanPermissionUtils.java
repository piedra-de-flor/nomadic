package com.example.Triple_clone.domain.plan.application;

import com.example.Triple_clone.common.error.AuthErrorCode;
import com.example.Triple_clone.common.error.RestApiException;
import com.example.Triple_clone.common.logging.logMessage.PlanLogMessage;
import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.domain.Plan;
import com.example.Triple_clone.domain.plan.domain.PlanShare;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanPermissionUtils {
    private final PlanShareService planShareService;

    public boolean hasViewPermission(Plan plan, Member member) {
        hasOwnership(plan, member);
        Optional<PlanShare> planShare = planShareService.findByPlanIdAndMemberId(plan.getId(), member.getId());
        return planShare.isPresent() && planShare.get().canView();
    }

    public boolean hasEditPermission(Plan plan, Member member) {
        hasOwnership(plan, member);
        Optional<PlanShare> planShare = planShareService.findByPlanIdAndMemberId(plan.getId(), member.getId());
        return planShare.isPresent() && planShare.get().canEdit();
    }

    public void hasSharedWith(PlanShare planShare, Member member) {
        if (!planShare.isSharedWith(member)) {
            log.warn(PlanLogMessage.PLAN_SHARE_AUTH_FAILED.format(member.getEmail(), planShare.getId()));
            throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
        }
    }

    public void hasOwnership(Plan plan, Member member) {
        if (!plan.isMine(member.getId())) {
            log.warn(PlanLogMessage.PLAN_ACCESS_FAILED.format(member.getEmail(), plan.getId()));
            throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
        }
    }
}