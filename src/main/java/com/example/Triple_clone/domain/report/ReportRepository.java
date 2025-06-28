package com.example.Triple_clone.domain.report;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    boolean existsByTargetTypeAndTargetIdAndReporterId(ReportTargetType targetType, Long targetId, Long reporterId);

    long countByTargetTypeAndTargetId(ReportTargetType targetType, Long targetId);
}
