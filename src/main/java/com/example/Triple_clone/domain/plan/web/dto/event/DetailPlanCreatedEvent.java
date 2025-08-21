package com.example.Triple_clone.domain.plan.web.dto.event;

import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.domain.ChangeType;
import com.example.Triple_clone.domain.plan.domain.DetailPlan;
import com.example.Triple_clone.domain.plan.domain.Plan;
import com.example.Triple_clone.domain.plan.domain.TargetType;
import com.example.Triple_clone.domain.plan.web.dto.DetailPlanChangeData;
import lombok.Getter;

@Getter
public class DetailPlanCreatedEvent extends PlanChangeEvent {
    private final DetailPlan detailPlan;

    public DetailPlanCreatedEvent(Object source, Plan plan, Member changedBy, DetailPlan detailPlan) {
        super(source, plan, changedBy, ChangeType.CREATED, detailPlan.getId(), TargetType.DETAIL_PLAN);
        this.detailPlan = detailPlan;
    }

    @Override
    public Object getChangeData() {
        return DetailPlanChangeData.builder()
                .location(detailPlan.getLocation())
                .date(detailPlan.getDate())
                .time(detailPlan.getTime())
                .build();
    }
}