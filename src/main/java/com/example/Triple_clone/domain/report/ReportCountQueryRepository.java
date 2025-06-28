package com.example.Triple_clone.domain.report;

import com.example.Triple_clone.domain.report.ReportTargetType;
import com.example.Triple_clone.domain.report.report.ReportCountDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportCountQueryRepository {
    Page<ReportCountDto> searchReportCounts(ReportTargetType targetType, Long minReportCount, Pageable pageable);
}

