package com.example.Triple_clone.repository;

import com.example.Triple_clone.domain.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    boolean existsByReviewIdAndReporterId(Long commentId, Long reporterId);

    Optional<Report> findByReviewIdAndReporterId(Long commentId, Long reporterId);
}
