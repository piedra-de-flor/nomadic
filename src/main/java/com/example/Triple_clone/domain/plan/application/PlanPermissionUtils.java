package com.example.Triple_clone.domain.plan.application;

import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.domain.Plan;
import com.example.Triple_clone.domain.plan.domain.PlanShare;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlanPermissionUtils {
    private final PlanShareService planShareService;

    public boolean hasViewPermission(Plan plan, Member member) {
        if (plan.isMine(member.getId())) {
            return true;
        }

        Optional<PlanShare> planShare = planShareService.findByPlanIdAndMemberId(plan.getId(), member.getId());
        return planShare.isPresent() && planShare.get().canView();
    }

    public boolean hasEditPermission(Plan plan, Member member) {
        if (plan.isMine(member.getId())) {
            return true;
        }

        Optional<PlanShare> planShare = planShareService.findByPlanIdAndMemberId(plan.getId(), member.getId());
        return planShare.isPresent() && planShare.get().canEdit();
    }
}