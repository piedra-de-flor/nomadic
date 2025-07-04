package com.example.Triple_clone.domain.plan.application;

import com.example.Triple_clone.common.logging.logMessage.PlanLogMessage;
import com.example.Triple_clone.domain.plan.web.dto.DetailPlanUpdateDto;
import com.example.Triple_clone.domain.plan.domain.DetailPlan;
import com.example.Triple_clone.domain.plan.infra.DetailPlanRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DetailPlanService {
    private final DetailPlanRepository repository;

    public DetailPlan findById(long detailPlanId) {
        return repository.findById(detailPlanId)
                .orElseThrow(() -> {
                    log.warn(PlanLogMessage.DETAIL_PLAN_SEARCH_FAILED.format(detailPlanId));
                    return new EntityNotFoundException("new plan Entity");
                });
    }

    public void save(DetailPlan detailPlan) {
        repository.save(detailPlan);
    }

    public void update(DetailPlan detailPlan, DetailPlanUpdateDto updateDto) {
        detailPlan.update(updateDto.location(),
                updateDto.date(),
                updateDto.time());
    }

    public void delete(DetailPlan detailPlan) {
        repository.delete(detailPlan);
    }
}
