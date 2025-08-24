package com.example.Triple_clone.domain.plan.application;

import com.example.Triple_clone.common.error.AuthErrorCode;
import com.example.Triple_clone.common.error.RestApiException;
import com.example.Triple_clone.domain.member.application.UserService;
import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.domain.ChangeType;
import com.example.Triple_clone.domain.plan.domain.Plan;
import com.example.Triple_clone.domain.plan.domain.PlanChangeHistory;
import com.example.Triple_clone.domain.plan.web.dto.planhistory.PlanChangeHistoryResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanChangeHistoryFacadeService {
    private final PlanChangeHistoryService historyService;
    private final PlanService planService;
    private final UserService userService;
    private final PlanPermissionUtils planPermissionUtils;

    public Page<PlanChangeHistoryResponseDto> getPlanHistory(Long planId, String email, Pageable pageable) {
        validateViewPermission(planId, email);

        Page<PlanChangeHistory> histories = historyService.findByPlanId(planId, pageable);
        return histories.map(PlanChangeHistoryResponseDto::new);
    }

    public Page<PlanChangeHistoryResponseDto> getChangesByType(Long planId, ChangeType changeType, String email, Pageable pageable) {
        validateViewPermission(planId, email);

        Page<PlanChangeHistory> histories = historyService.findByPlanIdAndChangeType(planId, changeType, pageable);
        return histories.map(PlanChangeHistoryResponseDto::new);
    }

    public Page<PlanChangeHistoryResponseDto> getUserActivity(String email, Pageable pageable) {
        Member member = userService.findByEmail(email);

        Page<PlanChangeHistory> histories = historyService.findByMemberId(member.getId(), pageable);
        return histories.map(PlanChangeHistoryResponseDto::new);
    }

    private void validateViewPermission(Long planId, String email) {
        Member member = userService.findByEmail(email);
        Plan plan = planService.findById(planId);

        if (!planPermissionUtils.hasViewPermission(plan, member)) {
            throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
        }
    }
}