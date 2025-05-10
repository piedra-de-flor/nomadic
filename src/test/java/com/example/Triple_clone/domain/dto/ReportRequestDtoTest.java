package com.example.Triple_clone.domain.dto;

import com.example.Triple_clone.domain.vo.ReportingReason;
import com.example.Triple_clone.dto.report.ReportRequestDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReportRequestDtoTest {
    @Test
    void createReportRequest() {
        ReportRequestDto request = new ReportRequestDto(1L, ReportingReason.SPAM, "스팸입니다");

        assertThat(request.reporterId()).isEqualTo(1L);
        assertThat(request.reason()).isEqualTo(ReportingReason.SPAM);
        assertThat(request.detail()).isEqualTo("스팸입니다");
    }
}