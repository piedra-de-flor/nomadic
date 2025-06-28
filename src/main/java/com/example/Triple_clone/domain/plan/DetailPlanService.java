package com.example.Triple_clone.domain.plan;

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
                    log.warn("⚠️ 세부 계획 조회 실패 - 존재하지 않는 세부 계획: {}", detailPlanId);
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
