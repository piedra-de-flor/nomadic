package com.example.Triple_clone.service.planning;

import com.example.Triple_clone.domain.entity.DetailPlan;
import com.example.Triple_clone.domain.entity.Plan;
import com.example.Triple_clone.dto.planning.DetailPlanDto;
import com.example.Triple_clone.dto.planning.DetailPlanUpdateDto;
import com.example.Triple_clone.repository.DetailPlanRepository;
import com.example.Triple_clone.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DetailPlanService {
    private final PlanRepository planRepository;
    private final DetailPlanRepository detailPlanRepository;
    public DetailPlanDto create(DetailPlanDto detailPlanDto) {
        Plan plan = planRepository.findById(detailPlanDto.planId())
                .orElseThrow(() -> new NoSuchElementException("no plan Entity"));

        DetailPlan detailPlan = detailPlanDto.toEntity(plan);
        detailPlanRepository.save(detailPlan);
        return detailPlanDto;
    }

    public DetailPlanDto update(DetailPlanUpdateDto updateDto) {
        Plan plan = planRepository.findById(updateDto.planId())
                .orElseThrow(() -> new NoSuchElementException("no plan Entity"));

        DetailPlan detailPlan = plan.getPlans().stream()
                .filter(detail -> detail.getId() == updateDto.detailPlanId())
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("no detail plan in this plan"));

        detailPlan.update(
                updateDto.location(),
                updateDto.date(),
                updateDto.time());

        return new DetailPlanDto(plan.getId(),
                detailPlan.getLocation(),
                detailPlan.getDate(),
                detailPlan.getTime());
    }

    public DetailPlan delete(long planId, long detailPlanId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new NoSuchElementException("no plan Entity"));

        DetailPlan detailPlan = plan.getPlans().stream()
                .filter(detail -> detail.getId() == detailPlanId)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("no detail plan in this plan"));

        detailPlanRepository.delete(detailPlan);
        return detailPlan;
    }
}
