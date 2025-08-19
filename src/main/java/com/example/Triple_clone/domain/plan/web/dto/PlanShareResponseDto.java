package com.example.Triple_clone.domain.plan.web.dto;

import com.example.Triple_clone.domain.plan.domain.PlanShare;
import com.example.Triple_clone.domain.plan.domain.ShareRole;
import com.example.Triple_clone.domain.plan.domain.ShareStatus;

import java.time.LocalDateTime;

public record PlanShareResponseDto(
        Long shareId,
        Long planId,
        String planName,
        String memberEmail,
        String memberName,
        ShareRole role,
        ShareStatus status,
        LocalDateTime sharedAt,
        LocalDateTime acceptedAt
) {
    public PlanShareResponseDto(PlanShare planShare) {
        this(
                planShare.getId(),
                planShare.getPlan().getId(),
                planShare.getPlan().getPlace(),
                planShare.getMember().getEmail(),
                planShare.getMember().getName(),
                planShare.getRole(),
                planShare.getStatus(),
                planShare.getSharedAt(),
                planShare.getAcceptedAt()
        );
    }
}