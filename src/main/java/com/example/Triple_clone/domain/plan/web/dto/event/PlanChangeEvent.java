package com.example.Triple_clone.domain.plan.web.dto.event;

import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.domain.ChangeType;
import com.example.Triple_clone.domain.plan.domain.Plan;
import com.example.Triple_clone.domain.plan.domain.TargetType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public abstract class PlanChangeEvent extends ApplicationEvent {
    private final Plan plan;
    private final Member changedBy;
    private final ChangeType changeType;
    private final Long targetId;
    private final TargetType targetType;

    public PlanChangeEvent(Object source, Plan plan, Member changedBy,
                           ChangeType changeType, Long targetId, TargetType targetType) {
        super(source);
        this.plan = plan;
        this.changedBy = changedBy;
        this.changeType = changeType;
        this.targetId = targetId;
        this.targetType = targetType;
    }

    public abstract Object getChangeData();
}