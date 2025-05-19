package com.example.Triple_clone.repository;

import com.example.Triple_clone.domain.entity.ReportCount;
import com.example.Triple_clone.domain.vo.ReportTargetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportCountRepository extends JpaRepository<ReportCount, Long> {
    Optional<ReportCount> findByTargetIdAndTargetType(Long targetId, ReportTargetType targetType);
}
