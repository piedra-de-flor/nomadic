package com.example.Triple_clone.domain.entity;

import com.example.Triple_clone.domain.member.Member;
import com.example.Triple_clone.domain.vo.ReportTargetType;
import com.example.Triple_clone.domain.vo.ReportingReason;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReportTest {

    @Test
    void reportEntityCreation() {
        Member reporter = new Member();
        Report report = Report.builder()
                .reporter(reporter)
                .targetId(1L)
                .targetType(ReportTargetType.REVIEW)
                .reason(ReportingReason.INAPPROPRIATE)
                .detail("불쾌한 내용입니다")
                .build();

        assertThat(report.getReporter()).isEqualTo(reporter);
        assertThat(report.getTargetId()).isEqualTo(1L);
        assertThat(report.getTargetType()).isEqualTo(ReportTargetType.REVIEW);
        assertThat(report.getReason()).isEqualTo(ReportingReason.INAPPROPRIATE);
        assertThat(report.getDetail()).isEqualTo("불쾌한 내용입니다");
    }
}
