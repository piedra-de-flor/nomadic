package com.example.Triple_clone.domain.plan.web.dto.notification;

import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.domain.Plan;
import lombok.Getter;

@Getter
public class PlanShareRejectedNotificationEvent extends PlanNotificationEvent {

    public PlanShareRejectedNotificationEvent(Plan plan, Member rejecter, Member planOwner) {
        super(plan, rejecter, planOwner, "REJECTED");
    }

    @Override
    public String generateMessage() {
        return String.format("%s님이 '%s' 계획 공유를 거절했습니다.",
                getActor().getName(),
                getPlan().getPlace());
    }
}
