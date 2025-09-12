package com.example.Triple_clone.domain.plan.web.dto.notification;

import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.domain.Plan;
import lombok.Getter;

@Getter
public class PlanShareAcceptedNotificationEvent extends PlanNotificationEvent {

    public PlanShareAcceptedNotificationEvent(Plan plan, Member accepter, Member planOwner) {
        super(plan, accepter, planOwner, "ACCEPTED");
    }

    @Override
    public String generateMessage() {
        return String.format("%s님이 '%s' 계획 공유를 수락했습니다.",
                getActor().getName(),
                getPlan().getPlace());
    }
}
