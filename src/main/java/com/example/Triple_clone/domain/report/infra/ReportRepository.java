package com.example.Triple_clone.domain.report.infra;

import com.example.Triple_clone.domain.report.domain.ReportTargetType;
import com.example.Triple_clone.domain.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    boolean existsByTargetTypeAndTargetIdAndReporterId(ReportTargetType targetType, Long targetId, Long reporterId);

    long countByTargetTypeAndTargetId(ReportTargetType targetType, Long targetId);
}
