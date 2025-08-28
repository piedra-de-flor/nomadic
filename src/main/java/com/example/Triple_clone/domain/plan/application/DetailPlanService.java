package com.example.Triple_clone.domain.plan.application;

import com.example.Triple_clone.common.logging.logMessage.PlanLogMessage;
import com.example.Triple_clone.domain.plan.web.dto.detailplan.DetailPlanData;
import com.example.Triple_clone.domain.plan.web.dto.detailplan.DetailPlanUpdateDto;
import com.example.Triple_clone.domain.plan.domain.DetailPlan;
import com.example.Triple_clone.domain.plan.infra.DetailPlanRepository;
import com.example.Triple_clone.domain.plan.web.dto.detailplan.DetailPlanUpdateResultDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public DetailPlanUpdateResultDto update(DetailPlan detailPlan, DetailPlanUpdateDto updateDto) {
        DetailPlanData beforeDto = DetailPlanData.from(detailPlan);

        detailPlan.update(updateDto.location(), updateDto.date(), updateDto.time());
        DetailPlan savedDetailPlan = repository.save(detailPlan);

        DetailPlanData afterDto = DetailPlanData.from(savedDetailPlan);

        return new DetailPlanUpdateResultDto(beforeDto, afterDto);
    }

    public void delete(DetailPlan detailPlan) {
        repository.delete(detailPlan);
    }
}
