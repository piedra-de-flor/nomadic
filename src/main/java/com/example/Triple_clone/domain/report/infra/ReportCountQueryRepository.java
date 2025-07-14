package com.example.Triple_clone.domain.report.infra;

import com.example.Triple_clone.domain.report.domain.ReportTargetType;
import com.example.Triple_clone.domain.report.web.dto.ReportCountDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportCountQueryRepository {
    Page<ReportCountDto> searchReportCounts(ReportTargetType targetType, Long minReportCount, Pageable pageable);
}

