package com.example.Triple_clone.domain.plan.web.dto.detailplan.event;

import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.domain.ChangeType;
import com.example.Triple_clone.domain.plan.domain.Plan;
import com.example.Triple_clone.domain.plan.domain.TargetType;
import com.example.Triple_clone.domain.plan.web.dto.detailplan.DetailPlanData;
import com.example.Triple_clone.domain.plan.web.dto.detailplan.DetailPlanUpdateChangeData;
import com.example.Triple_clone.domain.plan.web.dto.plan.event.PlanChangeEvent;
import lombok.Getter;
@Getter
public class DetailPlanUpdatedEvent extends PlanChangeEvent {
    private final DetailPlanData detailPlan;
    private final DetailPlanData oldDetailPlan;

    public DetailPlanUpdatedEvent(Object source, Plan plan, Member changedBy,
                                  DetailPlanData detailPlan, DetailPlanData oldDetailPlan) {
        super(source, plan, changedBy, ChangeType.UPDATED, detailPlan.id(), TargetType.DETAIL_PLAN);
        this.detailPlan = detailPlan;
        this.oldDetailPlan = oldDetailPlan;
    }

    @Override
    public Object getChangeData() {
        return DetailPlanUpdateChangeData.builder()
                .oldLocation(oldDetailPlan.location())
                .newLocation(detailPlan.location())
                .oldDate(oldDetailPlan.date())
                .newDate(detailPlan.date())
                .oldTime(oldDetailPlan.time())
                .newTime(detailPlan.time())
                .build();
    }
}