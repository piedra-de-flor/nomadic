package com.example.Triple_clone.domain.plan.application;

import com.example.Triple_clone.common.logging.logMessage.PlanLogMessage;
import com.example.Triple_clone.domain.accommodation.domain.Accommodation;
import com.example.Triple_clone.domain.plan.domain.DetailPlan;
import com.example.Triple_clone.domain.plan.domain.Plan;
import com.example.Triple_clone.domain.plan.web.dto.DetailPlanDto;
import com.example.Triple_clone.domain.plan.web.dto.DetailPlanUpdateDto;
import com.example.Triple_clone.domain.plan.web.dto.ReservationCreateDto;
import com.example.Triple_clone.domain.recommend.domain.Recommendation;
import com.example.Triple_clone.common.error.AuthErrorCode;
import com.example.Triple_clone.domain.accommodation.application.AccommodationService;
import com.example.Triple_clone.domain.recommend.application.RecommendService;
import com.example.Triple_clone.common.error.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DetailPlanFacadeService {
    private final PlanService planService;
    private final DetailPlanService detailPlanService;
    private final RecommendService recommendService;
    private final AccommodationService accommodationService;

    public DetailPlanDto create(DetailPlanDto detailPlanDto) {
        Plan plan = planService.findById(detailPlanDto.planId());
        DetailPlan detailPlan = detailPlanDto.toEntity(plan);

        detailPlanService.save(detailPlan);

        return detailPlanDto;
    }

    public DetailPlanDto addRecommendation(long recommendationId, long planId) {
        Plan plan = planService.findById(planId);
        Recommendation recommendation = recommendService.findById(recommendationId);

        DetailPlanDto detailPlanDto = new DetailPlanDto(planId, recommendation.getLocation(), plan.getStartDay(), null);
        this.create(detailPlanDto);

        return detailPlanDto;
    }

    public ReservationCreateDto createReservation(ReservationCreateDto reservationCreateDto) {
        Plan plan = planService.findById(reservationCreateDto.planId());
        Accommodation accommodation = accommodationService.findById(reservationCreateDto.accommodationId());
        DetailPlan detailPlan = reservationCreateDto.toEntity(plan, accommodation);

        detailPlanService.save(detailPlan);

        return reservationCreateDto;
    }

    public List<DetailPlanDto> readAll(long planId) {
        Plan plan = planService.findById(planId);
        List<DetailPlanDto> response = new ArrayList<>();

        for (DetailPlan detailPlan : plan.getPlans()) {
            response.add(detailPlan.toDto());
        }
        return response;
    }

    public DetailPlanDto update(DetailPlanUpdateDto updateDto) {
        Plan plan = planService.findById(updateDto.planId());
        DetailPlan detailPlan = detailPlanService.findById(updateDto.detailPlanId());

        if (isContain(plan, detailPlan)) {
            detailPlanService.update(detailPlan, updateDto);

            return new DetailPlanDto(updateDto.planId(),
                    updateDto.location(),
                    updateDto.date(),
                    updateDto.time());
        }

        log.warn(PlanLogMessage.PLAN_DOESNT_HAVE_THE_DETAIL_PLAN.format("세부 계획 수정 실패", plan.getId(), detailPlan.getId()));
        throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
    }

    public DetailPlan delete(long planId, long detailPlanId) {
        Plan plan = planService.findById(planId);
        DetailPlan detailPlan = detailPlanService.findById(detailPlanId);

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
