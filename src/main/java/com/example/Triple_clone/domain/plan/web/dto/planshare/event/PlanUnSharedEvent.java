package com.example.Triple_clone.domain.plan.web.dto.planshare.event;

import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.domain.ChangeType;
import com.example.Triple_clone.domain.plan.domain.Plan;
import com.example.Triple_clone.domain.plan.domain.ShareRole;
import com.example.Triple_clone.domain.plan.domain.TargetType;
import com.example.Triple_clone.domain.plan.web.dto.plan.event.PlanChangeEvent;
import com.example.Triple_clone.domain.plan.web.dto.planshare.PlanShareChangeData;
import lombok.Getter;

@Getter
public class PlanUnSharedEvent extends PlanChangeEvent {
    private final Member sharedMember;
    private final ShareRole role;

    public PlanUnSharedEvent(Object source, Plan plan, Member sharer, Member sharedMember, ShareRole role) {
        super(source, plan, sharer, ChangeType.UNSHARED, sharedMember.getId(), TargetType.MEMBER);
        this.sharedMember = sharedMember;
        this.role = role;
    }

    @Override
    public Object getChangeData() {
        return PlanShareChangeData.builder()
                .sharedMemberEmail(sharedMember.getEmail())
                .sharedMemberName(sharedMember.getName())
                .role(role)
                .build();
    }
}