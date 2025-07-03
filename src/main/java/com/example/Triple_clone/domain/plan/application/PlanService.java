package com.example.Triple_clone.domain.plan.application;

import com.example.Triple_clone.domain.plan.web.dto.PlanStyleUpdateDto;
import com.example.Triple_clone.domain.plan.domain.Style;
import com.example.Triple_clone.domain.plan.domain.DetailPlan;
import com.example.Triple_clone.domain.plan.domain.Location;
import com.example.Triple_clone.domain.plan.domain.Partner;
import com.example.Triple_clone.domain.plan.domain.Plan;
import com.example.Triple_clone.domain.plan.infra.PlanRepository;
import com.example.Triple_clone.domain.plan.web.dto.PlanPartnerUpdateDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository repository;

    public List<DetailPlan> getPlans(long planId) {
        Plan plan = findById(planId);

        return plan.getPlans();
    }

    public Plan findById(long planId) {
        return repository.findById(planId)
                .orElseThrow(() -> {
                    log.warn("⚠️ 계획 조회 실패 - 존재하지 않는 계획: {}", planId);
                    return new EntityNotFoundException("no plan Entity");
                });
    }

    public void save(Plan plan) {
        repository.save(plan);
    }

    public void delete(Plan plan) {
        repository.delete(plan);
    }

    public void updateStyle(PlanStyleUpdateDto updateDto, long memberId) {
        Plan plan = findById(updateDto.planDto().planId());
        if (plan.isMine(memberId)) {
            plan.chooseStyle(Style.toStyles(updateDto.styles()));
            return;
        }

        log.warn("⚠️ 계획 스타일 수정 실패 - 계획 소유권 없음: user = {} / target = {}", memberId, plan.getId());
        throw new IllegalArgumentException("this plan is not yours");
    }

    public void updatePartner(PlanPartnerUpdateDto updateDto, long memberId) {
        Plan plan = findById(updateDto.planDto().planId());
        if (plan.isMine(memberId)) {
            plan.choosePartner(Partner.valueOf(updateDto.partner()));
            return;
        }

        log.warn("⚠️ 계획 동반자 수정 실패 - 계획 소유권 없음: user = {} / target = {}", memberId, plan.getId());
        throw new IllegalArgumentException("this plan is not yours");
    }

    public List<Location> addLocation(long planId, String name, double latitude, double longitude) {
        List<Location> locations = getLocation(planId);
        locations.add(new Location(latitude, longitude, name));
        return locations;
    }

    public List<Location> getLocation(long planId) {
        List<DetailPlan> plans = getPlans(planId);
        List<Location> locations = new ArrayList<>();

        for (DetailPlan plan : plans) {
            locations.add(plan.getLocation());
        }
        return locations;
    }
}