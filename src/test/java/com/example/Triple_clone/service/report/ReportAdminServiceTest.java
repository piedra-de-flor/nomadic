package com.example.Triple_clone.service.report;

import com.example.Triple_clone.domain.entity.Report;
import com.example.Triple_clone.domain.entity.ReportCount;
import com.example.Triple_clone.domain.vo.ReportTargetType;
import com.example.Triple_clone.dto.report.ReportCountDto;
import com.example.Triple_clone.dto.report.ReportResponseDto;
import com.example.Triple_clone.dto.report.ReportSearchDto;
import com.example.Triple_clone.repository.ReportAdminRepository;
import com.example.Triple_clone.repository.ReportCountQueryRepository;
import com.example.Triple_clone.repository.ReportCountRepository;
import com.example.Triple_clone.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReportAdminServiceTest {

    @InjectMocks
    private ReportAdminService reportAdminService;

    @Mock private ReportAdminRepository reportAdminRepository;
    @Mock private ReportRepository reportRepository;
    @Mock private ReportCountRepository reportCountRepository;
    @Mock private ReportCountQueryRepository reportCountQueryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("신고 승인 처리 성공")
    void approveReport_success() {
        Report report = mock(Report.class);
        when(reportRepository.findById(1L)).thenReturn(Optional.of(report));

        reportAdminService.approveReport(1L);

        verify(report).approve();
    }

    @Test
    @DisplayName("신고 거절 처리 성공")
    void rejectReport_success() {
        Report report = mock(Report.class);
        when(reportRepository.findById(1L)).thenReturn(Optional.of(report));

        reportAdminService.rejectReport(1L);

        verify(report).reject();
    }

    @Test
    @DisplayName("신고 목록 조회 성공")
    void getReports_success() {
        ReportSearchDto condition = new ReportSearchDto();
        Pageable pageable = PageRequest.of(0, 10);
        Page<ReportResponseDto> page = new PageImpl<>(List.of(mock(ReportResponseDto.class)));

        when(reportAdminRepository.searchReports(condition, pageable)).thenReturn(page);

        Page<ReportResponseDto> result = reportAdminService.getReports(condition, pageable);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("신고 누적 목록 조회 성공")
    void getReportCounts_success() {
        Pageable pageable = PageRequest.of(0, 10);
        ReportCount entity = ReportCount.builder().targetId(1L).targetType(ReportTargetType.REVIEW).count(5L).build();
        when(reportCountRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(entity)));

        List<ReportCountDto> result = reportAdminService.getReportCounts(pageable);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTargetId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("신고 누적 조건별 조회 성공")
    void getReportCountsByCondition_success() {
        Pageable pageable = PageRequest.of(0, 10);
        ReportCountDto dto = new ReportCountDto(1L, ReportTargetType.REVIEW.name(), 10L);

        when(reportCountQueryRepository.searchReportCounts(ReportTargetType.REVIEW, 5L, pageable))
                .thenReturn(new PageImpl<>(List.of(dto)));

        List<ReportCountDto> result = reportAdminService.getReportCountsByCondition(ReportTargetType.REVIEW, 5L, pageable);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTargetType()).isEqualTo(ReportTargetType.REVIEW.name());
    }

    @Test
    @DisplayName("특정 대상의 신고 조회 성공")
    void getReportsByTarget_success() {
        Pageable pageable = PageRequest.of(0, 10);
        when(reportAdminRepository.searchReportsByTarget(ReportTargetType.REVIEW, 1L, pageable))
                .thenReturn(new PageImpl<>(List.of(mock(ReportResponseDto.class))));

        Page<ReportResponseDto> result = reportAdminService.getReportsByTarget(ReportTargetType.REVIEW, 1L, pageable);

        assertThat(result).hasSize(1);
    }
}
