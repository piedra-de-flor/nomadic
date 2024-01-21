package com.example.Triple_clone.service.planning;

import com.example.Triple_clone.domain.entity.DetailPlan;
import com.example.Triple_clone.domain.entity.Plan;
import com.example.Triple_clone.dto.planning.DetailPlanDto;
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
    public DetailPlanDto createDetailPlan(DetailPlanDto detailPlanDto) {
        Plan plan = planRepository.findById(detailPlanDto.planId())
                .orElseThrow(() -> new NoSuchElementException("no plan Entity"));

        DetailPlan detailPlan = detailPlanDto.toEntity(plan);
        detailPlanRepository.save(detailPlan);
        return detailPlanDto;
    }
}
