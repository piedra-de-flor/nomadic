package com.example.Triple_clone.service.planning;

import com.example.Triple_clone.domain.entity.DetailPlan;
import com.example.Triple_clone.domain.entity.Plan;
import com.example.Triple_clone.domain.vo.Location;
import com.example.Triple_clone.domain.vo.Partner;
import com.example.Triple_clone.domain.vo.Style;
import com.example.Triple_clone.dto.planning.PlanPartnerUpdateDto;
import com.example.Triple_clone.dto.planning.PlanStyleUpdateDto;
import com.example.Triple_clone.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository repository;

    public List<DetailPlan> getPlans(long planId) {
        Plan plan = repository.findById(planId)
                .orElseThrow(() -> new NoSuchElementException("no plan Entity"));

        return plan.getPlans();
    }

    public Plan findById(long planId) {
        return repository.findById(planId)
                .orElseThrow(() -> new NoSuchElementException("no plan Entity"));
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
        throw new IllegalArgumentException("this plan is not yours");
    }

    public void updatePartner(PlanPartnerUpdateDto updateDto, long memberId) {
        Plan plan = findById(updateDto.planDto().planId());
        if (plan.isMine(memberId)) {
            plan.choosePartner(Partner.valueOf(updateDto.partner()));
            return;
        }
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