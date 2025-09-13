package com.example.Triple_clone.domain.plan.application;

import com.example.Triple_clone.common.error.AuthErrorCode;
import com.example.Triple_clone.common.error.RestApiException;
import com.example.Triple_clone.common.logging.logMessage.PlanLogMessage;
import com.example.Triple_clone.domain.member.application.UserService;
import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.domain.Plan;
import com.example.Triple_clone.domain.plan.domain.PlanShare;
import com.example.Triple_clone.domain.plan.web.dto.plan.*;
import com.example.Triple_clone.domain.plan.web.dto.plan.event.PlanCreatedEvent;
import com.example.Triple_clone.domain.plan.web.dto.plan.event.PlanDeletedEvent;
import com.example.Triple_clone.domain.plan.web.dto.plan.event.PlanPartnerUpdatedEvent;
import com.example.Triple_clone.domain.plan.web.dto.plan.event.PlanStyleUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlanFacadeService {
    private final UserService userService;
    private final PlanService planService;
    private final PlanShareService planShareService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public PlanCreateDto create(PlanCreateDto createDto, String email) {
        Member member = userService.findByEmail(email);
        Plan plan = createDto.toEntity(member);
        planService.save(plan);

        eventPublisher.publishEvent(new PlanCreatedEvent(this, plan, member));
        return createDto;
    }

    public PlanReadResponseDto findPlan(long planId, String email) {
        Member member = userService.findByEmail(email);
        Plan plan = planService.findById(planId);

        if (plan.isMine(member.getId()) || PlanPermissionUtils.hasViewPermission(plan, member, planShareService)) {
            return new PlanReadResponseDto(plan);
        }

        log.warn(PlanLogMessage.PLAN_ACCESS_FAILED.format(email, plan.getId()));
        throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
    }

    public PlanReadAllResponseDto findAllPlan(String email) {
        Member member = userService.findByEmail(email);
        List<PlanReadResponseDto> plans = new ArrayList<>();

        for (Plan plan : member.getPlans()) {
            plans.add(new PlanReadResponseDto(plan));
        }

        List<PlanShare> sharedPlans = planShareService.findByMemberId(member.getId());
        for (PlanShare planShare : sharedPlans) {
            if (planShare.canView()) {
                plans.add(new PlanReadResponseDto(planShare.getPlan()));
            }
        }

        return new PlanReadAllResponseDto(plans);
    }

    @Transactional
    public PlanStyleUpdateDto updateStyle(PlanStyleUpdateDto updateDto, String email) {
        Member member = userService.findByEmail(email);
        Plan plan = planService.findById(updateDto.planDto().planId());

        if (plan.isMine(member.getId()) || PlanPermissionUtils.hasEditPermission(plan, member, planShareService)) {
            List<String> oldStyles = plan.getStyles().stream()
                    .map(Enum::name)
                    .toList();
            planService.updateStyle(updateDto);

            eventPublisher.publishEvent(new PlanStyleUpdatedEvent(this, plan, member, oldStyles, updateDto.styles()));
            return updateDto;
        }

        log.warn(PlanLogMessage.PLAN_ACCESS_FAILED.format(email, plan.getId()));
        throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
    }

    @Transactional
    public PlanPartnerUpdateDto updatePartner(PlanPartnerUpdateDto updateDto, String email) {
        Member member = userService.findByEmail(email);
        Plan plan = planService.findById(updateDto.planDto().planId());

        if (plan.isMine(member.getId()) || PlanPermissionUtils.hasEditPermission(plan, member, planShareService)) {
            String oldPartner = plan.getPartner() != null ? plan.getPartner().name() : null;
            planService.updatePartner(updateDto);

            eventPublisher.publishEvent(new PlanPartnerUpdatedEvent(this, plan, member, oldPartner, updateDto.partner()));
            return updateDto;
        }

        log.warn(PlanLogMessage.PLAN_ACCESS_FAILED.format(email, plan.getId()));
        throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
    }

    @Transactional
    public PlanDto deletePlan(PlanDto deleteDto, String email) {
        Member member = userService.findByEmail(email);
        Plan plan = planService.findById(deleteDto.planId());

        if (plan.isMine(member.getId())) {
            planService.delete(plan);

            eventPublisher.publishEvent(new PlanDeletedEvent(this, plan, member));
            return deleteDto;
        }

        log.warn(PlanLogMessage.PLAN_ACCESS_FAILED.format(email, plan.getId()));
        throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
    }
}