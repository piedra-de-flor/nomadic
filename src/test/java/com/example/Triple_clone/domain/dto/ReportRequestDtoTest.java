package com.example.Triple_clone.domain.dto;

import com.example.Triple_clone.domain.report.ReportingReason;
import com.example.Triple_clone.domain.report.report.ReportRequestDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReportRequestDtoTest {
    @Test
    void createReportRequest() {
        ReportRequestDto request = new ReportRequestDto(ReportingReason.SPAM, "스팸입니다");

        assertThat(request.reason()).isEqualTo(ReportingReason.SPAM);
        assertThat(request.detail()).isEqualTo("스팸입니다");
    }
}