package com.example.Triple_clone.domain.report;

import com.example.Triple_clone.domain.member.Member;
import com.example.Triple_clone.domain.review.Review;
import com.example.Triple_clone.domain.review.ReviewStatus;
import com.example.Triple_clone.domain.report.report.ReportCreatedEvent;
import com.example.Triple_clone.domain.report.report.ReportResponseDto;
import com.example.Triple_clone.domain.member.MemberRepository;
import com.example.Triple_clone.domain.review.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public ReportResponseDto reportReview(Long reviewId, String email, ReportingReason reason, String detail) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> {
                    log.warn("⚠️ 리뷰 신고 실패 - 존재하지 않는 리뷰: {}", reviewId);
                    return new EntityNotFoundException("no user entity");
                });

        if (review.getStatus() == ReviewStatus.DELETED) {
            log.warn("⚠️ 리뷰 신고 실패 - 이미 삭제된 신고: {}", reviewId);
            throw new IllegalArgumentException("삭제된 리뷰는 신고할 수 없습니다.");
        }

        Member reporter = memberRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("⚠️ 리뷰 신고 실패 - 존재하지 않는 신고자: {}", email);
                    return new EntityNotFoundException("no user entity");
                });

        if (reportRepository.existsByTargetTypeAndTargetIdAndReporterId(ReportTargetType.REVIEW, review.getId(), reporter.getId())) {
            log.warn("⚠️ 리뷰 신고 실패 - 중복된 신고: reporter = {} / target = {}", reporter, reviewId);
            throw new IllegalArgumentException("이미 신고한 리뷰입니다.");
        }

        Report report = Report.of(review, reporter, reason, detail);
        reportRepository.save(report);

        long reportCount = reportRepository.countByTargetTypeAndTargetId(report.getTargetType(), report.getTargetId());

        eventPublisher.publishEvent(new ReportCreatedEvent(report, reportCount));
        return ReportResponseDto.from(report);
    }
}
