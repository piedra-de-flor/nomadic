package com.example.Triple_clone.domain.report.web.dto;

import com.example.Triple_clone.domain.report.domain.ReportStatus;
import com.example.Triple_clone.domain.report.domain.ReportTargetType;
import com.example.Triple_clone.domain.report.domain.ReportingReason;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ReportSearchDto {
    private ReportTargetType targetType;
    private ReportStatus status;
    private ReportingReason reason;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fromDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime toDate;
}
