package com.example.Triple_clone.domain.plan.application;

import com.example.Triple_clone.common.error.AuthErrorCode;
import com.example.Triple_clone.common.error.DuplicatedProcessErrorCode;
import com.example.Triple_clone.common.error.RestApiException;
import com.example.Triple_clone.common.logging.logMessage.PlanLogMessage;
import com.example.Triple_clone.domain.member.application.UserService;
import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.domain.Plan;
import com.example.Triple_clone.domain.plan.domain.PlanShare;
import com.example.Triple_clone.domain.plan.domain.ShareRole;
import com.example.Triple_clone.domain.plan.web.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanShareFacadeService {
    private final PlanShareService planShareService;
    private final PlanService planService;
    private final UserService userService;

    @Transactional
    public PlanShareCreateDto sharePlan(PlanShareCreateDto createDto, String ownerEmail) {
        Member owner = userService.findByEmail(ownerEmail);
        Plan plan = planService.findById(createDto.planId());

        if (!plan.isMine(owner.getId())) {
            log.warn(PlanLogMessage.PLAN_ACCESS_FAILED.format(ownerEmail, plan));
            throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
        }

        Member sharedMember = userService.findByEmail(createDto.sharedMemberEmail());

        if (planShareService.isAlreadyShared(plan.getId(), sharedMember.getId())) {
            throw new RestApiException(DuplicatedProcessErrorCode.DUPLICATED_PROCESS_ERROR_CODE);
        }

        PlanShare planShare = PlanShare.builder()
                .plan(plan)
                .member(sharedMember)
                .role(ShareRole.valueOf(createDto.role()))
                .build();

        planShareService.save(planShare);
        return createDto;
    }

    public List<PlanShareResponseDto> getSharedPlans(String email) {
        Member member = userService.findByEmail(email);
        List<PlanShare> planShares = planShareService.findByMemberId(member.getId());

        return planShares.stream()
                .map(PlanShareResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<PlanShareResponseDto> getPendingShares(String email) {
        Member member = userService.findByEmail(email);
        List<PlanShare> pendingShares = planShareService.findPendingSharesByMemberId(member.getId());

        return pendingShares.stream()
                .map(PlanShareResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<PlanShareResponseDto> getPlanSharedMembers(Long planId, String email) {
        Member member = userService.findByEmail(email);
        Plan plan = planService.findById(planId);
        List<PlanShare> planShares = planShareService.findByPlanId(plan.getId());

        if (planShares.stream().noneMatch(planShare -> planShare.isSharedWith(member))) {
            log.warn(PlanLogMessage.PLAN_ACCESS_FAILED.format(email, planId));
            throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
        }

        return planShares.stream()
                .map(PlanShareResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PlanShareResponseDto acceptShare(Long shareId, String email) {
        Member member = userService.findByEmail(email);
        PlanShare planShare = planShareService.findById(shareId);

        if (!planShare.isSharedWith(member)) {
            log.warn(PlanLogMessage.PLAN_SHARE_AUTH_FAILED.format(email, shareId));
            throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
        }

        PlanShare acceptedShare = planShareService.acceptShare(shareId);
        return new PlanShareResponseDto(acceptedShare);
    }

    @Transactional
    public PlanShareResponseDto rejectShare(Long shareId, String email) {
        Member member = userService.findByEmail(email);
        PlanShare planShare = planShareService.findById(shareId);

        if (!planShare.isSharedWith(member)) {
            log.warn(PlanLogMessage.PLAN_SHARE_AUTH_FAILED.format(email, shareId));
            throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
        }

        PlanShare rejectedShare = planShareService.rejectShare(shareId);
        return new PlanShareResponseDto(rejectedShare);
    }

    @Transactional
    public void removeShare(Long shareId, String email) {
        Member member = userService.findByEmail(email);
        PlanShare planShare = planShareService.findById(shareId);
        Plan plan = planShare.getPlan();

        if (!plan.isMine(member.getId())) {
            log.warn(PlanLogMessage.PLAN_ACCESS_FAILED.format(email, plan.getId()));
            throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
        }

        if (!planShare.isSharedWith(member)) {
            log.warn(PlanLogMessage.PLAN_SHARE_AUTH_FAILED.format(email, shareId));
            throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
        }

        planShareService.delete(planShare);
    }
}