package com.example.Triple_clone.domain.plan.application;

import com.example.Triple_clone.common.logging.logMessage.PlanLogMessage;
import com.example.Triple_clone.domain.plan.web.dto.plan.PlanStyleUpdateDto;
import com.example.Triple_clone.domain.plan.domain.Style;
import com.example.Triple_clone.domain.plan.domain.DetailPlan;
import com.example.Triple_clone.domain.plan.domain.Location;
import com.example.Triple_clone.domain.plan.domain.Partner;
import com.example.Triple_clone.domain.plan.domain.Plan;
import com.example.Triple_clone.domain.plan.infra.PlanRepository;
import com.example.Triple_clone.domain.plan.web.dto.plan.PlanPartnerUpdateDto;
import com.example.Triple_clone.domain.plan.web.dto.plan.PlanUpdateDto;
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
                    log.warn(PlanLogMessage.PLAN_SEARCH_FAILED.format(planId));
                    return new EntityNotFoundException("no plan Entity");
                });
    }

    public void save(Plan plan) {
        repository.save(plan);
    }

    public void delete(Plan plan) {
        repository.delete(plan);
    }

    public void updateStyle(PlanStyleUpdateDto updateDto) {
        Plan plan = findById(updateDto.planDto().planId());
        plan.chooseStyle(Style.toStyles(updateDto.styles()));
    }

    public void updatePartner(PlanPartnerUpdateDto updateDto) {
        Plan plan = findById(updateDto.planDto().planId());
        plan.choosePartner(Partner.valueOf(updateDto.partner()));
    }

    public void updateNameAndDate(PlanUpdateDto updateDto) {
        Plan plan = findById(updateDto.planDto().planId());

        if (!updateDto.name().isEmpty()) {
            plan.updateName(updateDto.name());
        }

        if (!updateDto.start().equals(plan.getStartDay()) || !updateDto.start().equals(plan.getEndDay())) {
            plan.updateDate(updateDto.start(), updateDto.end());
        }
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