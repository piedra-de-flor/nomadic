package com.example.Triple_clone.service.report;

import com.example.Triple_clone.domain.member.Member;
import com.example.Triple_clone.domain.member.MemberRepository;
import com.example.Triple_clone.domain.report.*;
import com.example.Triple_clone.domain.review.Review;
import com.example.Triple_clone.domain.review.ReviewRepository;
import com.example.Triple_clone.domain.review.ReviewStatus;
import com.example.Triple_clone.domain.report.report.ReportCreatedEvent;
import com.example.Triple_clone.domain.report.report.ReportResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 리뷰를_정상적으로_신고한다() {
        Long reviewId = 1L;
        String email = "test@example.com";
        ReportingReason reason = ReportingReason.SPAM;
        String detail = "스팸 신고입니다.";

        Member reporter = new Member("test", email, "password", new ArrayList<>());

        Review review = mock(Review.class);
        when(review.getStatus()).thenReturn(ReviewStatus.ACTIVE);
        when(review.getId()).thenReturn(reviewId);
        when(review.getReportType()).thenReturn(ReportTargetType.REVIEW);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(reporter));
        when(reportRepository.existsByTargetTypeAndTargetIdAndReporterId(ReportTargetType.REVIEW, reviewId, reporter.getId()))
                .thenReturn(false);
        when(reportRepository.countByTargetTypeAndTargetId(ReportTargetType.REVIEW, reviewId))
                .thenReturn(0L);

        ReportResponseDto response = reportService.reportReview(reviewId, email, reason, detail);

        verify(reportRepository).save(any(Report.class));
        verify(eventPublisher).publishEvent(any(ReportCreatedEvent.class));
        assertThat(response).isNotNull();
        assertThat(response.reason()).isEqualTo(reason);
    }

    @Test
    void 존재하지_않는_리뷰를_신고하면_예외가_발생한다() {
        Long reviewId = 1L;
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reportService.reportReview(reviewId, "test@example.com", ReportingReason.SPAM, "detail"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("리뷰가 존재하지 않습니다.");
    }

    @Test
    void 삭제된_리뷰는_신고할_수_없다() {
        Long reviewId = 1L;
        Review review = mock(Review.class);
        when(review.getStatus()).thenReturn(ReviewStatus.DELETED);
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        assertThatThrownBy(() -> reportService.reportReview(reviewId, "test@example.com", ReportingReason.SPAM, "detail"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("삭제된 리뷰는 신고할 수 없습니다.");
    }

    @Test
    void 존재하지_않는_사용자가_신고하면_예외가_발생한다() {
        Long reviewId = 1L;
        Review review = mock(Review.class);
        when(review.getStatus()).thenReturn(ReviewStatus.ACTIVE);
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reportService.reportReview(reviewId, "test@example.com", ReportingReason.SPAM, "detail"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("신고자가 존재하지 않습니다.");
    }

    @Test
    void 이미_신고한_리뷰는_중복_신고할_수_없다() {
        Long reviewId = 1L;
        String email = "test@example.com";
        Member member = new Member("test", email, "pw", new ArrayList<>());

        Review review = mock(Review.class);
        when(review.getStatus()).thenReturn(ReviewStatus.ACTIVE);
        when(review.getId()).thenReturn(reviewId);
        when(review.getReportType()).thenReturn(ReportTargetType.REVIEW);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));
        when(reportRepository.existsByTargetTypeAndTargetIdAndReporterId(ReportTargetType.REVIEW, reviewId, member.getId()))
                .thenReturn(true);

        assertThatThrownBy(() -> reportService.reportReview(reviewId, email, ReportingReason.SPAM, "detail"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 신고한 리뷰입니다.");
    }
}
