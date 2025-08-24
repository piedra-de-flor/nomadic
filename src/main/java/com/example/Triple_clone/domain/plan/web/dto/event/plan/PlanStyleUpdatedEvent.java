package com.example.Triple_clone.domain.plan.web.dto.event.plan;

import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.domain.ChangeType;
import com.example.Triple_clone.domain.plan.domain.Plan;
import com.example.Triple_clone.domain.plan.domain.TargetType;
import com.example.Triple_clone.domain.plan.web.dto.PlanStyleChangeData;
import com.example.Triple_clone.domain.plan.web.dto.event.PlanChangeEvent;
import lombok.Getter;

import java.util.List;

@Getter
public class PlanStyleUpdatedEvent extends PlanChangeEvent {
    private final List<String> oldStyles;
    private final List<String> newStyles;

    public PlanStyleUpdatedEvent(Object source, Plan plan, Member changedBy,
                                 List<String> oldStyles, List<String> newStyles) {
        super(source, plan, changedBy, ChangeType.UPDATED, plan.getId(), TargetType.PLAN_STYLE);
        this.oldStyles = oldStyles;
        this.newStyles = newStyles;
    }

    @Override
    public Object getChangeData() {
        return PlanStyleChangeData.builder()
                .oldStyles(oldStyles)
                .newStyles(newStyles)
                .build();
    }
}
