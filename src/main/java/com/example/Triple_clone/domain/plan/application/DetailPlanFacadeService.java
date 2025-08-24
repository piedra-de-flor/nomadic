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
import com.example.Triple_clone.domain.recommend.application.RecommendService;
import com.example.Triple_clone.domain.recommend.domain.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final RecommendService recommendService;
    private final UserService userService;
    private final AccommodationQueryService accommodationService;
    private final PlanPermissionUtils planPermissionUtils;

    @Transactional
    public DetailPlanDto create(DetailPlanDto detailPlanDto, String email) {
        Plan plan = planService.findById(detailPlanDto.planId());
        DetailPlan detailPlan = detailPlanDto.toEntity(plan);
        Member member = userService.findByEmail(email);

        if (!planPermissionUtils.hasEditPermission(plan, member)) {
            log.warn(PlanLogMessage.PLAN_ACCESS_FAILED.format(email, plan));
            throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
        }

        detailPlanService.save(detailPlan);
        plan.addDetailPlan(detailPlan);
        return detailPlanDto;
    }

    @Transactional
    public DetailPlanDto addRecommendation(long recommendationId, long planId, String email) {
        Plan plan = planService.findById(planId);
        Recommendation recommendation = recommendService.findById(recommendationId);

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

        if (!planPermissionUtils.hasEditPermission(plan, member)) {
            log.warn(PlanLogMessage.PLAN_ACCESS_FAILED.format(email, plan));
            throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
        }

        detailPlanService.save(detailPlan);

        return reservationCreateDto;
    }

    public List<DetailPlanDto> readAll(long planId, String email) {
        Plan plan = planService.findById(planId);
        Member member = userService.findByEmail(email);
        List<DetailPlanDto> response = new ArrayList<>();

        if (!planPermissionUtils.hasViewPermission(plan, member)) {
            log.warn(PlanLogMessage.PLAN_ACCESS_FAILED.format(email, plan));
            throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
        }

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

        if (!planPermissionUtils.hasEditPermission(plan, member)) {
            log.warn(PlanLogMessage.PLAN_ACCESS_FAILED.format(email, plan));
            throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
        }

        if (isContain(plan, detailPlan)) {
            DetailPlan updatedDetailPlan = detailPlanService.update(detailPlan, updateDto);

            return new DetailPlanDto(
                    updatedDetailPlan.getId(),
                    updatedDetailPlan.getLocation(),
                    updatedDetailPlan.getDate(),
                    updatedDetailPlan.getTime(),
                    updatedDetailPlan.getVersion()
                    );
        }

        log.warn(PlanLogMessage.PLAN_DOESNT_HAVE_THE_DETAIL_PLAN.format("세부 계획 수정 실패", plan.getId(), detailPlan.getId()));
        throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
    }

    @Transactional
    public DetailPlan delete(long planId, long detailPlanId, String email) {
        Plan plan = planService.findById(planId);
        DetailPlan detailPlan = detailPlanService.findById(detailPlanId);
        Member member = userService.findByEmail(email);

        if (!planPermissionUtils.hasEditPermission(plan, member)) {
            log.warn(PlanLogMessage.PLAN_ACCESS_FAILED.format(email, plan));
            throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
        }

        if (isContain(plan, detailPlan)) {
            detailPlanService.delete(detailPlan);
            return detailPlan;
        }

        log.warn(PlanLogMessage.PLAN_DOESNT_HAVE_THE_DETAIL_PLAN.format("세부 계획 삭제 실패", plan.getId(), detailPlan.getId()));
        throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
    }

    private boolean isContain(Plan plan, DetailPlan detailPlan) {
        return planService.getPlans(plan.getId()).contains(detailPlan);
    }
}