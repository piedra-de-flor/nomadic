package com.example.Triple_clone.domain.plan.web.dto.plan.event;

import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.domain.ChangeType;
import com.example.Triple_clone.domain.plan.domain.Plan;
import com.example.Triple_clone.domain.plan.domain.TargetType;
import com.example.Triple_clone.domain.plan.web.dto.plan.PlanCreateChangeData;
import lombok.Getter;

@Getter
public class PlanCreatedEvent extends PlanChangeEvent {
    public PlanCreatedEvent(Object source, Plan plan, Member changedBy) {
        super(source, plan, changedBy, ChangeType.CREATED, plan.getId(), TargetType.PLAN);
    }

    @Override
    public Object getChangeData() {
        return PlanCreateChangeData.builder()
                .place(getPlan().getPlace())
                .partner(getPlan().getPartner() != null ? getPlan().getPartner().name() : null)
                .styles(getPlan().getStyles().stream().map(Enum::name).collect(java.util.stream.Collectors.toList()))
                .startDay(getPlan().getStartDay())
                .endDay(getPlan().getEndDay())
                .build();
    }
}