package com.example.Triple_clone.domain.plan.web.dto.event.detailplan;

import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.domain.ChangeType;
import com.example.Triple_clone.domain.plan.domain.DetailPlan;
import com.example.Triple_clone.domain.plan.domain.Plan;
import com.example.Triple_clone.domain.plan.domain.TargetType;
import com.example.Triple_clone.domain.plan.web.dto.DetailPlanUpdateChangeData;
import com.example.Triple_clone.domain.plan.web.dto.event.PlanChangeEvent;
import lombok.Getter;
@Getter
public class DetailPlanUpdatedEvent extends PlanChangeEvent {
    private final DetailPlan detailPlan;
    private final DetailPlan oldDetailPlan;

    public DetailPlanUpdatedEvent(Object source, Plan plan, Member changedBy,
                                  DetailPlan detailPlan, DetailPlan oldDetailPlan) {
        super(source, plan, changedBy, ChangeType.UPDATED, detailPlan.getId(), TargetType.DETAIL_PLAN);
        this.detailPlan = detailPlan;
        this.oldDetailPlan = oldDetailPlan;
    }

    @Override
    public Object getChangeData() {
        return DetailPlanUpdateChangeData.builder()
                .oldLocation(oldDetailPlan.getLocation())
                .newLocation(detailPlan.getLocation())
                .oldDate(oldDetailPlan.getDate())
                .newDate(detailPlan.getDate())
                .oldTime(oldDetailPlan.getTime())
                .newTime(detailPlan.getTime())
                .build();
    }
}