package com.example.Triple_clone.domain.plan.application;

import com.example.Triple_clone.common.error.DuplicatedProcessErrorCode;
import com.example.Triple_clone.common.error.RestApiException;
import com.example.Triple_clone.domain.member.application.UserService;
import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.domain.Plan;
import com.example.Triple_clone.domain.plan.domain.PlanShare;
import com.example.Triple_clone.domain.plan.domain.ShareRole;
import com.example.Triple_clone.domain.plan.web.dto.planshare.PlanShareCreateDto;
import com.example.Triple_clone.domain.plan.web.dto.planshare.PlanShareResponseDto;
import com.example.Triple_clone.domain.plan.web.dto.planshare.event.PlanSharedEvent;
import com.example.Triple_clone.domain.plan.web.dto.planshare.event.PlanUnSharedEvent;
import com.example.Triple_clone.domain.plan.web.dto.planshare.event.ShareAcceptedEvent;
import com.example.Triple_clone.domain.plan.web.dto.planshare.event.ShareRejectedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public PlanShareCreateDto sharePlan(PlanShareCreateDto createDto, String ownerEmail) {
        Member owner = userService.findByEmail(ownerEmail);
        Plan plan = planService.findById(createDto.planId());

        PlanPermissionUtils.validateOwnership(plan, owner);

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

        eventPublisher.publishEvent(new PlanSharedEvent(this, plan, owner, sharedMember, ShareRole.valueOf(createDto.role())));
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

        for (PlanShare planShare : planShares) {
            PlanPermissionUtils.validateSharedWith(planShare, member);
        }

        return planShares.stream()
                .map(PlanShareResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PlanShareResponseDto acceptShare(Long shareId, String email) {
        Member member = userService.findByEmail(email);
        PlanShare planShare = planShareService.findById(shareId);

        PlanPermissionUtils.validateSharedWith(planShare, member);

        PlanShare acceptedShare = planShareService.acceptShare(shareId);

        eventPublisher.publishEvent(new ShareAcceptedEvent(this, planShare.getPlan(), member, planShare));
        return new PlanShareResponseDto(acceptedShare);
    }

    @Transactional
    public PlanShareResponseDto rejectShare(Long shareId, String email) {
        Member member = userService.findByEmail(email);
        PlanShare planShare = planShareService.findById(shareId);

        PlanPermissionUtils.validateSharedWith(planShare, member);

        PlanShare rejectedShare = planShareService.rejectShare(shareId);

        eventPublisher.publishEvent(new ShareRejectedEvent(this, planShare.getPlan(), member, planShare));
        return new PlanShareResponseDto(rejectedShare);
    }

    @Transactional
    public void removeShare(Long shareId, String email) {
        Member member = userService.findByEmail(email);
        PlanShare planShare = planShareService.findById(shareId);
        Plan plan = planShare.getPlan();

        PlanPermissionUtils.validateOwnership(plan, member);
        PlanPermissionUtils.validateSharedWith(planShare, member);

        planShareService.delete(planShare);

        eventPublisher.publishEvent(new PlanUnSharedEvent(this, plan, member, planShare.getMember(), planShare.getRole()));
    }
}