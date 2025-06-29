package com.example.Triple_clone.domain.report.web.dto;

import com.example.Triple_clone.domain.report.domain.ReportStatus;
import com.example.Triple_clone.domain.report.domain.ReportingReason;
import com.example.Triple_clone.domain.report.domain.Report;
import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;

public record ReportResponseDto(
        Long id,
        String targetType,
        Long targetId,
        String reporterName,
        ReportingReason reason,
        ReportStatus status,
        LocalDateTime createdAt
) {
    @QueryProjection
    public ReportResponseDto {
    }

    public static ReportResponseDto from(Report report) {
        return new ReportResponseDto(
                report.getId(),
                report.getTargetType().name(),
                report.getTargetId(),
                report.getReporter().getName(),
                report.getReason(),
                report.getStatus(),
                report.getCreatedAt()
        );
    }
}

