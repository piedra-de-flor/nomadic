package com.example.Triple_clone.domain.entity;

import com.example.Triple_clone.domain.vo.ReportTargetType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReportCountTest {

    @Test
    @DisplayName("ReportCount 객체가 빌더로 정상 생성되어야 한다")
    void buildReportCount() {
        ReportCount reportCount = ReportCount.builder()
                .targetId(1L)
                .targetType(ReportTargetType.REVIEW)
                .count(3L)
                .build();

        assertThat(reportCount.getTargetId()).isEqualTo(1L);
        assertThat(reportCount.getTargetType()).isEqualTo(ReportTargetType.REVIEW);
        assertThat(reportCount.getCount()).isEqualTo(3L);
    }

    @Test
    @DisplayName("신고 카운트가 1 증가되어야 한다")
    void incrementReportCount() {
        ReportCount reportCount = ReportCount.builder()
                .targetId(2L)
                .targetType(ReportTargetType.COMMENT)
                .count(5L)
                .build();

        reportCount.incrementCount();

        assertThat(reportCount.getCount()).isEqualTo(6L);
    }
}
