package com.example.Triple_clone.domain.plan.web.dto.event.planshare;

import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.domain.ChangeType;
import com.example.Triple_clone.domain.plan.domain.Plan;
import com.example.Triple_clone.domain.plan.domain.PlanShare;
import com.example.Triple_clone.domain.plan.domain.TargetType;
import com.example.Triple_clone.domain.plan.web.dto.ShareStatusChangeData;
import com.example.Triple_clone.domain.plan.web.dto.event.PlanChangeEvent;
import lombok.Getter;

@Getter
public class ShareAcceptedEvent extends PlanChangeEvent {
    private final PlanShare planShare;

    public ShareAcceptedEvent(Object source, Plan plan, Member member, PlanShare planShare) {
        super(source, plan, member, ChangeType.UPDATED, planShare.getId(), TargetType.PLAN_SHARE);
        this.planShare = planShare;
    }

    @Override
    public Object getChangeData() {
        return ShareStatusChangeData.builder()
                .sharedMemberEmail(getChangedBy().getEmail())
                .sharedMemberName(getChangedBy().getName())
                .role(planShare.getRole())
                .oldStatus("PENDING")
                .newStatus("ACCEPTED")
                .build();
    }
}
