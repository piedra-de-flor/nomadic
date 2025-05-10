package com.example.Triple_clone.repository;

import com.example.Triple_clone.dto.report.ReportCountDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportCountQueryRepository {
    Page<ReportCountDto> searchReportCounts(String targetType, Long minReportCount, Pageable pageable);
}

