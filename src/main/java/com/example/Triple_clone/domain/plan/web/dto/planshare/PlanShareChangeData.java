package com.example.Triple_clone.domain.plan.web.dto.planshare;

import com.example.Triple_clone.domain.plan.domain.ShareRole;
import lombok.Builder;

@Builder
public record PlanShareChangeData(
        String sharedMemberEmail,
        String sharedMemberName,
        ShareRole role
) {}