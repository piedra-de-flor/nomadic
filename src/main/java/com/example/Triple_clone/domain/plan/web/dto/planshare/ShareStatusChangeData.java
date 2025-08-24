package com.example.Triple_clone.domain.plan.web.dto.planshare;

import com.example.Triple_clone.domain.plan.domain.ShareRole;
import lombok.Builder;

@Builder
public record ShareStatusChangeData(
        String sharedMemberEmail,
        String sharedMemberName,
        ShareRole role,
        String oldStatus,
        String newStatus
) {}