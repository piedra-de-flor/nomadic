package com.example.Triple_clone.domain.plan.web.dto.planhistory;

import com.example.Triple_clone.domain.plan.domain.ChangeType;
import com.example.Triple_clone.domain.plan.domain.PlanChangeHistory;

import java.time.LocalDateTime;

public record PlanChangeHistoryResponseDto(
        Long id,
        Long planId,
        String changedByName,
        String changedByEmail,
        ChangeType changeType,
        String changeTypeDescription,
        Long targetId,
        String targetType,
        String changeData,
        LocalDateTime createdAt
) {
    public PlanChangeHistoryResponseDto(PlanChangeHistory history) {
        this(
                history.getId(),
                history.getPlan().getId(),
                history.getChangedBy().getName(),
                history.getChangedBy().getEmail(),
                history.getChangeType(),
                history.getChangeType().getDescription(),
                history.getTargetId(),
                history.getTargetType(),
                history.getChangeData(),
                history.getCreatedAt()
        );
    }
}