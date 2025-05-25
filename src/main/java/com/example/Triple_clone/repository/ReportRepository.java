package com.example.Triple_clone.repository;

import com.example.Triple_clone.domain.entity.Report;
import com.example.Triple_clone.domain.vo.ReportTargetType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    boolean existsByTargetTypeAndTargetIdAndReporterId(ReportTargetType targetType, Long targetId, Long reporterId);

    long countByTargetTypeAndTargetId(ReportTargetType targetType, Long targetId);
}
