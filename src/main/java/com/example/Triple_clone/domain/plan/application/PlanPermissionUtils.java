package com.example.Triple_clone.domain.plan.application;

import com.example.Triple_clone.common.error.AuthErrorCode;
import com.example.Triple_clone.common.error.RestApiException;
import com.example.Triple_clone.common.logging.logMessage.PlanLogMessage;
import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.domain.Plan;
import com.example.Triple_clone.domain.plan.domain.PlanShare;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class PlanPermissionUtils {

    private PlanPermissionUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static void validateOwnership(Plan plan, Member member) {
        if (!plan.isMine(member.getId())) {
            log.warn(PlanLogMessage.PLAN_ACCESS_FAILED.format(member.getEmail(), plan.getId()));
            throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
        }
    }

    public static void validateSharedWith(PlanShare planShare, Member member) {
        if (!planShare.isSharedWith(member)) {
            log.warn(PlanLogMessage.PLAN_SHARE_AUTH_FAILED.format(member.getEmail(), planShare.getId()));
            throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
        }
    }

    public static void validateEditPermission(Plan plan, Member member, PlanShareService planShareService) {
        if (plan.isMine(member.getId())) {
            return;
        }

        planShareService.findByPlanIdAndMemberId(plan.getId(), member.getId())
                .filter(PlanShare::canEdit)
                .orElseThrow(() -> {
                    log.warn(PlanLogMessage.PLAN_ACCESS_FAILED.format(member.getEmail(), plan.getId()));
                    return new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
                });
    }

    public static void validateViewPermission(Plan plan, Member member, PlanShareService planShareService) {
        if (plan.isMine(member.getId())) {
            return;
        }

        planShareService.findByPlanIdAndMemberId(plan.getId(), member.getId())
                .filter(PlanShare::canView)
                .orElseThrow(() -> {
                    log.warn(PlanLogMessage.PLAN_ACCESS_FAILED.format(member.getEmail(), plan.getId()));
                    return new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
                });
    }

    public static boolean hasViewPermission(Plan plan, Member member, PlanShareService planShareService) {
        if (plan.isMine(member.getId())) {
            return true;
        }

        return planShareService.findByPlanIdAndMemberId(plan.getId(), member.getId())
                .map(PlanShare::canView)
                .orElse(false);
    }

    public static boolean hasEditPermission(Plan plan, Member member, PlanShareService planShareService) {
        if (plan.isMine(member.getId())) {
            return true;
        }

        return planShareService.findByPlanIdAndMemberId(plan.getId(), member.getId())
                .map(PlanShare::canEdit)
                .orElse(false);
    }
}