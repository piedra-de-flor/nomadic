package com.example.Triple_clone.domain.plan.web.dto.event.plan;

import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.domain.ChangeType;
import com.example.Triple_clone.domain.plan.domain.Plan;
import com.example.Triple_clone.domain.plan.domain.TargetType;
import com.example.Triple_clone.domain.plan.web.dto.PlanPartnerChangeData;
import com.example.Triple_clone.domain.plan.web.dto.event.PlanChangeEvent;
import lombok.Getter;

@Getter
public class PlanPartnerUpdatedEvent extends PlanChangeEvent {
    private final String oldPartner;
    private final String newPartner;

    public PlanPartnerUpdatedEvent(Object source, Plan plan, Member changedBy,
                                   String oldPartner, String newPartner) {
        super(source, plan, changedBy, ChangeType.UPDATED, plan.getId(), TargetType.PLAN_PARTNER);
        this.oldPartner = oldPartner;
        this.newPartner = newPartner;
    }

    @Override
    public Object getChangeData() {
        return PlanPartnerChangeData.builder()
                .oldPartner(oldPartner)
                .newPartner(newPartner)
                .build();
    }
}
