package com.example.Triple_clone.service.planning;

import com.example.Triple_clone.domain.entity.DetailPlan;
import com.example.Triple_clone.domain.entity.Plan;
import com.example.Triple_clone.dto.planning.DetailPlanDto;
import com.example.Triple_clone.dto.planning.DetailPlanUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class DetailPlanFacadeService {
    private final PlanService planService;
    private final DetailPlanService detailPlanService;

    public DetailPlanDto create(DetailPlanDto detailPlanDto) {
        Plan plan = planService.findById(detailPlanDto.planId());
        DetailPlan detailPlan = detailPlanDto.toEntity(plan);

        detailPlanService.save(detailPlan);

        return detailPlanDto;
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

        throw new NoSuchElementException("this detail plan id is not yours");
    }

    public DetailPlan delete(long planId, long detailPlanId) {
        Plan plan = planService.findById(planId);
        DetailPlan detailPlan = detailPlanService.findById(detailPlanId);

        if (isContain(plan, detailPlan)) {
            detailPlanService.delete(detailPlan);
            return detailPlan;
        }

        throw new NoSuchElementException("this detail plan id is not yours");
    }

    private boolean isContain(Plan plan, DetailPlan detailPlan) {
        return planService.getPlans(plan.getId()).contains(detailPlan);
    }
}
