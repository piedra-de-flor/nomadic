package com.example.Triple_clone.service.report;

import com.example.Triple_clone.domain.entity.Member;
import com.example.Triple_clone.domain.entity.Review;
import com.example.Triple_clone.domain.vo.ReportTargetType;
import com.example.Triple_clone.domain.vo.ReportingReason;
import com.example.Triple_clone.repository.MemberRepository;
import com.example.Triple_clone.repository.ReportCountRepository;
import com.example.Triple_clone.repository.ReportRepository;
import com.example.Triple_clone.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReportCountRepository reportCountRepository;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void reportReview_success() {
        Long reviewId = 1L;
        Long reporterId = 2L;
        ReportingReason reason = ReportingReason.OFFENSIVE_LANGUAGE;
        String detail = "혐오 발언입니다.";

        Member reporter = new Member("test", "test", "test", new ArrayList<>());
        Review review = mock(Review.class);

        when(reportRepository.existsByTargetTypeAndTargetIdAndReporterId("REVIEW", reviewId, reporterId)).thenReturn(false);
        when(memberRepository.findByEmail(reporter.getEmail())).thenReturn(java.util.Optional.of(reporter));
        when(reviewRepository.findById(reviewId)).thenReturn(java.util.Optional.of(review));
        when(review.getId()).thenReturn(reviewId);
        when(review.getReportType()).thenReturn(ReportTargetType.REVIEW);

        reportService.reportReview(reviewId, reporter.getEmail(), reason, detail);

        verify(reportRepository, times(1)).save(any());
    }
}
