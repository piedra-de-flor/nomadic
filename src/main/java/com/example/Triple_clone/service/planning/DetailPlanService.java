package com.example.Triple_clone.service.planning;

import com.example.Triple_clone.domain.entity.DetailPlan;
import com.example.Triple_clone.dto.planning.DetailPlanDto;
import com.example.Triple_clone.dto.planning.DetailPlanUpdateDto;
import com.example.Triple_clone.repository.DetailPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DetailPlanService {
    private final DetailPlanRepository repository;

    public DetailPlan findById(long detailPlanId) {
        return repository.findById(detailPlanId)
                .orElseThrow(() -> {
                    log.warn("⚠️ 세부 계획 조회 실패 - 존재하지 않는 세부 계획: {}", detailPlanId);
                    return new NoSuchElementException("new plan Entity");
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
