package com.example.Triple_clone.domain.plan.web.dto.notification;

import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.domain.Plan;
import lombok.Getter;

@Getter
public abstract class PlanNotificationEvent {
    protected final Plan plan;
    protected final Member actor;
    protected final Member receiver;
    protected final String action;

    protected PlanNotificationEvent(Plan plan, Member actor, Member receiver, String action) {
        this.plan = plan;
        this.actor = actor;
        this.receiver = receiver;
        this.action = action;
    }

    public abstract String generateMessage();
}
