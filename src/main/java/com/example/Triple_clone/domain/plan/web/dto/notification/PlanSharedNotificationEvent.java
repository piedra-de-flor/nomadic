package com.example.Triple_clone.domain.plan.web.dto.notification;

import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.domain.Plan;
import com.example.Triple_clone.domain.plan.domain.ShareRole;
import lombok.Getter;

@Getter
public class PlanSharedNotificationEvent extends PlanNotificationEvent {
    private final ShareRole role;

    public PlanSharedNotificationEvent(Plan plan, Member sharer, Member receiver, ShareRole role) {
        super(plan, sharer, receiver, "SHARED");
        this.role = role;
    }

    @Override
    public String generateMessage() {
        return String.format("%s님이 '%s' 계획을 %s 권한으로 공유했습니다.",
                getActor().getName(),
                getPlan().getPlace(),
                role.name());
    }
}
