package com.example.Triple_clone.dto.report;

import com.example.Triple_clone.domain.vo.ReportStatus;
import com.example.Triple_clone.domain.vo.ReportingReason;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ReportSearchDto {
    private String targetType;
    private ReportStatus status;
    private ReportingReason reason;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fromDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime toDate;
}
