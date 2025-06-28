package com.example.Triple_clone.domain.report.report;

import com.example.Triple_clone.domain.report.Report;
import com.example.Triple_clone.domain.report.ReportStatus;
import com.example.Triple_clone.domain.report.ReportingReason;
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

