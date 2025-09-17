package com.example.Triple_clone.domain.plan.application;

import com.example.Triple_clone.common.error.AuthErrorCode;
import com.example.Triple_clone.common.error.RestApiException;
import com.example.Triple_clone.common.logging.logMessage.PlanLogMessage;
import com.example.Triple_clone.domain.accommodation.application.AccommodationQueryService;
import com.example.Triple_clone.domain.accommodation.domain.Accommodation;
import com.example.Triple_clone.domain.member.application.UserService;
import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.domain.DetailPlan;
import com.example.Triple_clone.domain.plan.domain.Plan;
import com.example.Triple_clone.domain.plan.web.dto.detailplan.DetailPlanDto;
import com.example.Triple_clone.domain.plan.web.dto.detailplan.DetailPlanUpdateDto;
import com.example.Triple_clone.domain.plan.web.dto.ReservationCreateDto;
import com.example.Triple_clone.domain.plan.web.dto.detailplan.DetailPlanUpdateResultDto;
import com.example.Triple_clone.domain.plan.web.dto.detailplan.event.DetailPlanCreatedEvent;
import com.example.Triple_clone.domain.plan.web.dto.detailplan.event.DetailPlanDeletedEvent;
import com.example.Triple_clone.domain.plan.web.dto.detailplan.event.DetailPlanUpdatedEvent;
import com.example.Triple_clone.domain.recommend.application.RecommendQueryService;
import com.example.Triple_clone.domain.recommend.domain.Recommendation;
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
public class DetailPlanFacadeService {
    private final PlanService planService;
    private final DetailPlanService detailPlanService;
    private final RecommendQueryService recommendQueryService;
    private final UserService userService;
    private final AccommodationQueryService accommodationService;
    private final PlanShareService planShareService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public DetailPlanDto create(DetailPlanDto detailPlanDto, String email) {
        Plan plan = planService.findById(detailPlanDto.planId());
        DetailPlan detailPlan = detailPlanDto.toEntity(plan);
        Member member = userService.findByEmail(email);

        PlanPermissionUtils.validateEditPermission(plan, member, planShareService);

        detailPlanService.save(detailPlan);
        plan.addDetailPlan(detailPlan);

        eventPublisher.publishEvent(new DetailPlanCreatedEvent(this, plan, member, detailPlan));
        return detailPlanDto;
    }

    @Transactional
    public DetailPlanDto addRecommendation(long recommendationId, long planId, String email) {
        Plan plan = planService.findById(planId);
        Recommendation recommendation = recommendQueryService.findById(recommendationId);

        DetailPlanDto detailPlanDto = new DetailPlanDto(planId, recommendation.getLocation(), plan.getStartDay(), "00:00", null);
        this.create(detailPlanDto, email);

        return detailPlanDto;
    }

    @Transactional
    public ReservationCreateDto createReservation(ReservationCreateDto reservationCreateDto, String email) {
        Plan plan = planService.findById(reservationCreateDto.planId());
        Accommodation accommodation = accommodationService.findById(reservationCreateDto.accommodationId());
        DetailPlan detailPlan = reservationCreateDto.toEntity(plan, accommodation);
        Member member = userService.findByEmail(email);

        PlanPermissionUtils.validateEditPermission(plan, member, planShareService);

        detailPlanService.save(detailPlan);

        eventPublisher.publishEvent(new DetailPlanCreatedEvent(this, plan, member, detailPlan));
        return reservationCreateDto;
    }

    public List<DetailPlanDto> readAll(long planId, String email) {
        Plan plan = planService.findById(planId);
        Member member = userService.findByEmail(email);
        List<DetailPlanDto> response = new ArrayList<>();

        PlanPermissionUtils.validateViewPermission(plan, member, planShareService);

        for (DetailPlan detailPlan : plan.getPlans()) {
            response.add(detailPlan.toDto());
        }
        return response;
    }

    @Transactional
    public DetailPlanDto update(DetailPlanUpdateDto updateDto, String email) {
        Plan plan = planService.findById(updateDto.planId());
        DetailPlan detailPlan = detailPlanService.findById(updateDto.detailPlanId());
        Member member = userService.findByEmail(email);

        PlanPermissionUtils.validateEditPermission(plan, member, planShareService);

        if (isContain(plan, detailPlan)) {
            DetailPlanUpdateResultDto result = detailPlanService.update(detailPlan, updateDto);

            eventPublisher.publishEvent(new DetailPlanUpdatedEvent(
                    this, plan, member, result.after(), result.before()));

            return new DetailPlanDto(
                    updateDto.planId(),
                    result.after().location(),
                    result.after().date(),
                    result.after().time(),
                    result.after().version()
            );
        }

        log.warn(PlanLogMessage.PLAN_DOESNT_HAVE_THE_DETAIL_PLAN.format(
                "세부 계획 수정 실패", plan.getId(), detailPlan.getId()));
        throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
    }

    @Transactional
    public void delete(long planId, long detailPlanId, String email) {
        Plan plan = planService.findById(planId);
        DetailPlan detailPlan = detailPlanService.findById(detailPlanId);
        Member member = userService.findByEmail(email);

        PlanPermissionUtils.validateEditPermission(plan, member, planShareService);

        if (isContain(plan, detailPlan)) {
            detailPlanService.delete(detailPlan);

            eventPublisher.publishEvent(new DetailPlanDeletedEvent(this, plan, member, detailPlan));
            return;
        }

        log.warn(PlanLogMessage.PLAN_DOESNT_HAVE_THE_DETAIL_PLAN.format("세부 계획 삭제 실패", plan.getId(), detailPlan.getId()));
        throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
    }

    private boolean isContain(Plan plan, DetailPlan detailPlan) {
        return planService.getPlans(plan.getId()).contains(detailPlan);
    }
}